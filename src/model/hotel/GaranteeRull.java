package model.hotel;

import java.io.Serializable;

public class GaranteeRull implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 104L;
	public String GaranteeRulesTypeCode;
	public RuleValue RuleValues;
	public String Description;
	public String getGaranteeRulesTypeCode() {
		return GaranteeRulesTypeCode;
	}
	public void setGaranteeRulesTypeCode(String garanteeRulesTypeCode) {
		GaranteeRulesTypeCode = garanteeRulesTypeCode;
	}
	public RuleValue getRuleValues() {
		return RuleValues;
	}
	public void setRuleValues(RuleValue ruleValues) {
		RuleValues = ruleValues;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	
	
}
