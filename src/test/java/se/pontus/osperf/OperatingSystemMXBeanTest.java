package se.pontus.osperf;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.management.ManagementFactory;

import org.junit.jupiter.api.Test;

import com.sun.management.OperatingSystemMXBean;

class OperatingSystemMXBeanTest {

    @Test
    void osMxBeanTest() {
        assertTrue(ManagementFactory.getOperatingSystemMXBean() instanceof OperatingSystemMXBean);
        final OperatingSystemMXBean osMxBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        assertTrue(osMxBean.getCpuLoad() >= 0.0);
        assertTrue(osMxBean.getProcessCpuLoad() >= 0.0);
    }

}
