/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.dto;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;


/**
 * 
 * @author vincenzoingenito
 *
 *	DTO used to return validation result.
 */
@Getter
@Setter
public class DeletedSchedulerResDTO extends ResponseDTO {

	/**
	 * Serial version uid.
	 */
	private static final long serialVersionUID = -2144353497297675698L;
	 
	Map<String,Integer> output;
	
	public DeletedSchedulerResDTO() {
		super();
	}

	public DeletedSchedulerResDTO(final LogTraceInfoDTO traceInfo, final Map<String,Integer> inOutput) {
		super(traceInfo);
		output = inOutput;
	}
	
}
