package com.manda.usl.logger;

import com.google.auto.service.AutoService;
import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.helpers.NOPMDCAdapter;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

/**
 * @author hongda.li
 */
@AutoService(SLF4JServiceProvider.class)
public class ServiceProvider implements SLF4JServiceProvider {
    private static final String REQUESTED_API_VERSION = "2.0.9";
    private MDCAdapter mdcAdapter;
    private ILoggerFactory loggerFactory;
    private IMarkerFactory markerFactory;

    @Override
    public ILoggerFactory getLoggerFactory() {
        return this.loggerFactory;
    }

    @Override
    public IMarkerFactory getMarkerFactory() {
        return this.markerFactory;
    }

    @Override
    public MDCAdapter getMDCAdapter() {
        return this.mdcAdapter;
    }

    @Override
    public String getRequestedApiVersion() {
        return REQUESTED_API_VERSION;
    }

    @Override
    public void initialize() {
        this.mdcAdapter = new NOPMDCAdapter();
        this.loggerFactory = new LoggerFactory();
        this.markerFactory = new BasicMarkerFactory();
    }
}
