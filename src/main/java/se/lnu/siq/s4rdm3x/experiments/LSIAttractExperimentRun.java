package se.lnu.siq.s4rdm3x.experiments;

import se.lnu.siq.s4rdm3x.experiments.metric.Metric;
import se.lnu.siq.s4rdm3x.experiments.system.System;
import se.lnu.siq.s4rdm3x.model.CGraph;
import se.lnu.siq.s4rdm3x.model.cmd.mapper.ArchDef;
import se.lnu.siq.s4rdm3x.model.cmd.mapper.LSIAttractMapper;
import se.lnu.siq.s4rdm3x.model.cmd.util.FanInCache;

import java.util.Random;

public class LSIAttractExperimentRun extends IRExperimentRunBase {
    ExperimentRunData.IRMapperData m_exData;

    public LSIAttractExperimentRun(boolean a_doUseManualmapping, Data a_irData) {
        super(a_doUseManualmapping, a_irData);
    }


    @Override
    public ExperimentRun clone() {
        return new LSIAttractExperimentRun(doUseManualMapping(), getData());
    }

    @Override
    public ExperimentRunData.BasicRunData createNewRunData(Random a_rand) {
        m_exData = new ExperimentRunData.IRMapperData();
        getData().setRunDataVariables(m_exData, a_rand);
        return m_exData;
    }

    @Override
    public boolean runClustering(CGraph a_g, FanInCache fic, ArchDef a_arch) {

        LSIAttractMapper lsiam = new LSIAttractMapper(a_arch, doUseManualMapping(), m_exData.m_doUseCDA, m_exData.m_doUseNodeText, m_exData.m_doUseNodeName, m_exData.m_doUseArchComponentName, m_exData.m_minWordSize);

        lsiam.run(a_g);

        m_exData.m_totalManuallyClustered += lsiam.m_manuallyMappedNodes;
        m_exData.m_totalAutoWrong  += lsiam.m_autoWrong;
        m_exData.m_totalFailedClusterings  += lsiam.m_failedMappings;

        if (lsiam.m_automaticallyMappedNodes + lsiam.m_manuallyMappedNodes == 0) {
            return true;
        }

        m_exData.m_iterations++;
        return false;
    }
}