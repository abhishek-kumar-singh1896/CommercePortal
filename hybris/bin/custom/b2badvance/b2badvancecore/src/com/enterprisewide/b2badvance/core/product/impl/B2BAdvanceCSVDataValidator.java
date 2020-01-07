/**
 *
 */
package com.enterprisewide.b2badvance.core.product.impl;

import com.enterprisewide.b2badvance.core.dto.B2BAdvanceCsvDTO;


/**
 * This validator class is used to validate the DTO data
 *
 * @author Enterprise Wide
 */
public abstract class B2BAdvanceCSVDataValidator
{

	/**
	 * This method is used to validate and filter Variant Product attributes.
	 *
	 * @param dto
	 * @return
	 */
	public abstract B2BAdvanceCsvDTO validateCSVData(final B2BAdvanceCsvDTO dto);
}
