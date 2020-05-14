/**
 *
 */
package com.gallagher.facades.cmsitems.validator;

import static de.hybris.platform.cmsfacades.common.validator.ValidationErrorBuilder.newValidationErrorBuilder;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.FIELD_REQUIRED_L10N;
import static de.hybris.platform.cmsfacades.constants.CmsfacadesConstants.INVALID_URL_FORMAT;
import static org.apache.commons.lang3.StringUtils.isBlank;

import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cmsfacades.cmsitems.validator.DefaultCMSLinkComponentValidator;
import de.hybris.platform.commercefacades.storesession.data.LanguageData;

import java.util.Objects;

import org.apache.commons.validator.routines.UrlValidator;


/**
 * @author shilpiverma
 *
 */
public class GallagherCMSLinkComponentValidator extends DefaultCMSLinkComponentValidator
{
	@Override
	public void validate(final CMSLinkComponentModel validatee)
	{
		verifyOnlyOneTypeProvided(validatee);

		getLanguageFacade().getLanguages().stream() //
				.filter(LanguageData::isRequired) //
				.forEach(languageData -> {
					if (isBlank(validatee.getLinkName(getCommonI18NService().getLocaleForIsoCode(languageData.getIsocode()))))
					{
						getValidationErrorsProvider().getCurrentValidationErrors().add(newValidationErrorBuilder() //
								.field(CMSLinkComponentModel.LINKNAME) //
								.language(languageData.getIsocode()).errorCode(FIELD_REQUIRED_L10N) //
								.errorArgs(new Object[]
						{ languageData.getIsocode() }) //
								.build());
					}
				});


		if (Objects.nonNull(validatee.getUrl()) && !validatee.getUrl().startsWith("/")
				&& !UrlValidator.getInstance().isValid(validatee.getUrl()))
		{
			getValidationErrorsProvider().getCurrentValidationErrors().add(newValidationErrorBuilder() //
					.field(CMSLinkComponentModel.URL) //
					.errorCode(INVALID_URL_FORMAT) //
					.build());
		}
	}
}
