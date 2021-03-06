import se.lnu.siq.s4rdm3x.experiments.*;
import se.lnu.siq.s4rdm3x.experiments.system.System;

import se.lnu.siq.s4rdm3x.model.CGraph;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {


    private static class ExThread extends Thread {

        private int m_ix;
        String m_dir;
        RunFileSaver m_fs;
        ExperimentRunner m_exr;
        boolean m_doSaveMappings;

        public ExThread(ExperimentRunner a_exp, String a_dir, int a_index, boolean a_doSaveMappings) {
            m_ix = a_index;
            m_dir = a_dir;
            m_doSaveMappings = a_doSaveMappings;
            m_exr = a_exp;
        }

        public void run() {
            java.lang.System.out.print("" + m_ix + ", ");
            CGraph graph = new CGraph();
            m_fs = new RunFileSaver(m_dir, "" + m_ix + "data", m_doSaveMappings);
            m_exr.setRunListener(m_fs);
            m_exr.run(graph);
        }

        public ExperimentRunner.State getExState() {
            if (m_exr != null) {
                return m_exr.getState();
            } else {
                return ExperimentRunner.State.Idle;
            }
        }

        public void halt() {
            if (m_exr != null) {
                m_exr.stop();
            }
        }

        public int getRows() {
            if (m_fs != null) {
                return m_fs.getRunCount();
            } else {
                return 0;
            }
        }

    }

    private static ArrayList<ExThread> startThreads(ExperimentRunner a_exp, int a_threadCount, String a_file, boolean a_doSaveMappings) {
        ArrayList<ExThread> ret = new ArrayList<>();
        java.lang.System.out.print("Running experiments: ");
        for(int i = 0; i < a_threadCount; i++) {
            final int ix = i;

            ExThread r = new ExThread(new ExperimentRunner(a_exp), a_file, ix, a_doSaveMappings);
            ret.add(r);
            Thread t = new Thread(r);
            t.start();
        }
        return ret;
    }

    private static int sumRows(Iterable<ExThread> a_threads) {
        int rows = 0;

        for (ExThread et: a_threads) {
            rows += et.getRows();
        }

        return rows;
    }

    private static boolean allIdle(Iterable<ExThread> a_threads) {
        for (ExThread et: a_threads) {
            if (et.getExState() != ExperimentRunner.State.Idle) {
                return false;
            }
        }
        return true;
    }

    private static int getInitialRows(String a_dir) {
        String fileName = a_dir; // This is a hidden dependency to RunFileSaver
        try {
            int ret = 0;
            Stream<Path> paths = Files.walk(Paths.get(fileName));

            for (Path p : paths.filter(Files::isRegularFile).filter((path) -> path.toString().endsWith(".csv")).collect(Collectors.toList())) {
                ret += Files.readAllLines(p).size() - 1;    // first row is header...
            }

            return ret;

        } catch (Exception e) {
            return 0;
        }
    }

    private static void printUsage() {
        java.lang.System.out.println("Usage");
        java.lang.System.out.println("-experiment [path to experiment xml file. Mandatory]");
        java.lang.System.out.println("-threads [number of threads per experiment. Optional: default is 1]");
        java.lang.System.out.println("-count [number of data rows per experiment. Optional: default is 50000]");
        java.lang.System.out.println("-outDir [directory for output, it will be created if it does not exist. If data rows already exists they will be added to.]");
    }

    public static void main2(String[] a_args) {
        // accepted arguments
        // -threads thread count number optional default 1
        // -experiment experiment file
        // -mapping saves mappings (optional)
        // -count the number of rows to get (optional)
        final CmdArgsHandler args = new CmdArgsHandler(a_args);

        final String experiment = args.getArgumentString("-experiment");
        boolean saveMappings = args.getArgumentBool("-mapping", false);
        final int rowLimit = args.getArgumentInt("-count", 50000);
        final int threadCount = args.getArgumentInt("-threads", 1);
        final String outDir = args.getArgumentString("-outDir");

        final boolean argsError = experiment.length() == 0 || outDir.length() == 0;
        if (argsError) {
            java.lang.System.out.println("Arguments Error");
            printUsage();
            java.lang.System.exit(-1);
        }

        ExperimentXMLPersistence exp = new ExperimentXMLPersistence();
        ArrayList<ExperimentRunner> expRunners;

        try {
            ArrayList<ExperimentRunner> expRunnersWithOnlyOneSystem = new ArrayList<>();
            {
                expRunners = exp.loadExperimentRunners(experiment, null);

                // if the runners have more than one system we should divide them into different threads
                for (ExperimentRunner exr : expRunners) {
                    for (System sua : exr.getSystems()) {
                        ExperimentRunner newExr = new ExperimentRunner(exr, sua);
                        expRunnersWithOnlyOneSystem.add(newExr);
                    }
                }
            }


            int exCount = 0;

            class Exp {
                ArrayList<ExThread> m_threads;
                int m_rowLimit;
                private boolean m_done = false;
                ExperimentRunner m_runner;
                String m_dir = "";

                void quit() {
                    for (ExThread et : m_threads) {
                        et.halt();
                    }
                    m_done = true;

                    java.lang.System.out.println("All experiment threads done!");
                }

                boolean done() {
                    return m_done || sumRows(m_threads) > m_rowLimit;
                }
            }

            ArrayList<Exp> allRunningExperiments = new ArrayList<>();

            // start the experiments
            for (ExperimentRunner exr : expRunnersWithOnlyOneSystem) {
                Exp e = new Exp();
                String dir = outDir + File.separator + exr.getSystems().iterator().next().getName() + "_" + exCount;
                int initialRows = getInitialRows(dir);
                //e.m_threads = run(exr, threadCount, dir, saveMappings);
                e.m_threads = run(exr, threadCount, dir, saveMappings);
                e.m_runner = exr;
                e.m_rowLimit = (rowLimit * exr.getSystemCount())- initialRows;
                e.m_dir = dir;

                allRunningExperiments.add(e);
                exCount++;
            }

            // monitor the experiments
            while (allRunningExperiments.size() > 0) {
                for (int eIx = 0; eIx < allRunningExperiments.size();) {
                    Exp e = allRunningExperiments.get(eIx);
                    if (e.done()) {
                        allRunningExperiments.remove(e);
                        e.quit();

                        // we could now reallocate the experiment threads to other experiments...
                        for (int i = 0; i < threadCount; ) {
                            Exp notDone = null;
                            for (Exp ex : allRunningExperiments) {
                                if (!ex.done()) {
                                    notDone = ex;
                                    java.lang.System.out.println("Migrating threads to next experiment");
                                    notDone.m_threads.addAll(run(notDone.m_runner, 1, notDone.m_dir, saveMappings));
                                    i++;
                                }

                                if (i >= threadCount) {
                                    notDone = null;
                                    break;
                                }
                            }

                            if (notDone == null) {
                                break;
                            }
                        }
                    } else {
                        eIx++;
                    }
                }
            }

            java.lang.System.out.println("All experiments Done!");


        } catch (Exception e) {
            java.lang.System.out.println(e);
        }
    }

    public static ArrayList<ExThread>  run(ExperimentRunner a_experiment, int a_threadCount, String a_dir, boolean a_doSaveMappings) {
        if (a_threadCount < 1) {
            a_threadCount = 1;
        }
        java.lang.System.out.println("Running " + a_threadCount + " threads. Saving data in: " + a_dir);
        if (a_doSaveMappings) {
            java.lang.System.out.println("Also saving experiment mappings.");
        }
        ArrayList<ExThread> threads = startThreads(a_experiment, a_threadCount, a_dir, a_doSaveMappings);
        java.lang.System.out.println("\nAll experiment threads Started!");
        return threads;
    }


    public static void main(String[] a_args) {
        main2(a_args);
    }

}
