package com.japhet.application.residentsincome.util;

import javax.enterprise.inject.Produces;

public class PercentProducer {

	@Produces
	@Tenth
	private Double tenth = 0.1D;
	
	@Produces
	@FifteenPercent
	private Double fifteenPercent = 0.15D;
	
	@Produces
	@Hundredth
	private Double hundredth = 0.01D;
	
	
	@Produces
	@LongValue
	private Long residentId;
}
