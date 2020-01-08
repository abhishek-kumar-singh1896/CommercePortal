package com.enterprisewide.b2badvance.core.sftp;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Logger;


/**
 * This class is used for enabling and routing JSch logging into SLF4J
 *
 * @author Enterprise Wide
 */
public class B2BAdvanceJSchLogger implements Logger
{
	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(B2BAdvanceJSchLogger.class);
	private final Map<Integer, Consumer<String>> logMap = new HashMap<>(5);
	private final Map<Integer, BooleanSupplier> enabledMap = new HashMap<>(5);

	/**
	 * This constructor maps JSchLogger level to slf4j level and its enabled method via map.
	 */
	public B2BAdvanceJSchLogger()
	{
		logMap.put(DEBUG, LOG::debug);
		logMap.put(ERROR, LOG::error);
		logMap.put(FATAL, LOG::error);
		logMap.put(INFO, LOG::info);
		logMap.put(WARN, LOG::warn);

		enabledMap.put(com.jcraft.jsch.Logger.DEBUG, LOG::isDebugEnabled);
		enabledMap.put(com.jcraft.jsch.Logger.ERROR, LOG::isErrorEnabled);
		enabledMap.put(com.jcraft.jsch.Logger.FATAL, LOG::isErrorEnabled);
		enabledMap.put(com.jcraft.jsch.Logger.INFO, LOG::isInfoEnabled);
		enabledMap.put(com.jcraft.jsch.Logger.WARN, LOG::isWarnEnabled);
	}

	@Override
	public boolean isEnabled(final int level)
	{
		boolean isEnable = false;
		final BooleanSupplier enabler = enabledMap.get(level);
		if (enabler != null)
		{
			isEnable = enabledMap.get(level).getAsBoolean();
		}
		return isEnable;
	}

	@Override
	public void log(final int level, final String message)
	{
		final Consumer<String> operation = logMap.get(level);
		if (operation != null)
		{
			logMap.get(level).accept("JSch Setup : " + message);
		}
	}

}
