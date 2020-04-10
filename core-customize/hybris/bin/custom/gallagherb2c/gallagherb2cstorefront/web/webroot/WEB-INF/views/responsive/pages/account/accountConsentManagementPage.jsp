<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<spring:htmlEscape defaultHtmlEscape="true"/>
<c:url value="/my-account/consents" var="submitConsents" />
<div class="main__inner-wrapper">
        <div class="global-alerts">
        </div>
        <div class="account-section">
            <div class="yCmsContentSlot account-section-content">
                <div class="container">
                    <div class="row">
                        <div class="col-12">
                            <div class="breadcrumb-out">
                                <nav aria-label="breadcrumb">
                                    <ol class="breadcrumb">
                                        <li class="breadcrumb-item"><a href="#">Home</a></li>
                                        <li class="breadcrumb-item active" aria-current="page">Consent Management</li>
                                    </ol>
                                </nav>
                            </div>
                        </div>
                    </div>
                    <div class="profile-container">
                        <h1 class="primary-title">Consent Management</h1>

                        <div class="account-section-content">
                            <div class="account-section-form consent-section-form">
                                <div id="consent-management-form" data-consent-management-url="">
                                    <div class="consent-management-intro">
                                        <p> To personalize your experience, we'd like your consent to receive your
                                            profile data:</p>
                                    </div>
                                    
                                    <form:form modelAttribute="consentsPreferences" method="post" action="${submitConsents}" id="customerPreferences">

                                    <ul class="consent-management-list" role="tablist" aria-live="polite"
                                        data-behavior="accordion">

                                        <li class="consent-management-list__item is-expanded"
                                            data-binding="expand-accordion-item">
                                            <span tabindex="0" class="consent-management-list__title"
                                                aria-controls="panel1" role="tab" aria-selected="true"
                                                aria-expanded="true" data-binding="expand-accordion-trigger">
                                                <label for="newsLetters"
                                                    class="consent-management-list__label"><spring:theme code="text.preference.overlay.newsLetter" /></label>
                                            </span>
                                            <div id="consent-template-description-0" class="consent-management-list__content" role="tabpanel"
												aria-hidden="false" aria-labelledby="tab1" data-binding="expand-accordion-container style="height: 76px;">
												<div class="consent-management-list__content-inner">
													<p>
														<spring:theme code="text.preference.overlay.newsLetter.description" />
													</p>
												</div>
											</div>
                                            <div class="toggle-button">
												<form:checkbox tabindex="0" id="newsLetters"
													class="toggle-button__input" path="newsLetters" />
												<label for="newsLetters">
													<div class="toggle-button__switch"></div>
												</label>
											</div>
                                        </li>
                                        <li class="consent-management-list__item is-expanded"
                                            data-binding="expand-accordion-item">
                                            <span tabindex="1" class="consent-management-list__title"
                                                aria-controls="panel2" role="tab" aria-selected="true"
                                                aria-expanded="true" data-binding="expand-accordion-trigger">
                                                <label for="events"
                                                    class="consent-management-list__label"><spring:theme code="text.preference.overlay.events" /></label>
                                            </span>
                                            <div id="consent-template-description-1" class="consent-management-list__content" role="tabpanel"
												aria-hidden="false" aria-labelledby="tab1" data-binding="expand-accordion-container style="height: 76px;">
												<div class="consent-management-list__content-inner">
													<p>
														<spring:theme code="text.preference.overlay.events.description" />
													</p>
												</div>
											</div>
                                            <div class="toggle-button">
												<form:checkbox tabindex="0" id="events"
													class="toggle-button__input" path="events" />
												<label for="events">
													<div class="toggle-button__switch"></div>
												</label>
											</div>
                                        </li>
                                        <li class="consent-management-list__item is-expanded"
                                            data-binding="expand-accordion-item">
                                            <span tabindex="2" class="consent-management-list__title"
                                                aria-controls="panel3" role="tab" aria-selected="true"
                                                aria-expanded="true" data-binding="expand-accordion-trigger">
                                                <label for="productRelease"
                                                    class="consent-management-list__label"><spring:theme code="text.preference.overlay.productRelease" /></label>
                                            </span>
                                            <div id="consent-template-description-2" class="consent-management-list__content" role="tabpanel"
												aria-hidden="false" aria-labelledby="tab1" data-binding="expand-accordion-container style="height: 76px;">
												<div class="consent-management-list__content-inner">
													<p>
														<spring:theme code="text.preference.overlay.productRelease.description" />
													</p>
												</div>
											</div>
                                            <div class="toggle-button">
												<form:checkbox tabindex="0" id="productRelease"
													class="toggle-button__input" path="productRelease" />
												<label for="productRelease">
													<div class="toggle-button__switch"></div>
												</label>
											</div>
                                        </li>
                                        <li class="consent-management-list__item is-expanded"
                                            data-binding="expand-accordion-item">
                                            <span tabindex="3" class="consent-management-list__title"
                                                aria-controls="panel4" role="tab" aria-selected="true"
                                                aria-expanded="true" data-binding="expand-accordion-trigger">
                                                <label for="productUpdate"
                                                    class="consent-management-list__label"><spring:theme code="text.preference.overlay.productUpdate" /></label>
                                            </span>
                                            <div id="consent-template-description-3" class="consent-management-list__content" role="tabpanel"
												aria-hidden="false" aria-labelledby="tab1" data-binding="expand-accordion-container style="height: 76px;">
												<div class="consent-management-list__content-inner">
													<p>
														<spring:theme code="text.preference.overlay.productUpdate.description" />
													</p>
												</div>
											</div>
                                            <div class="toggle-button">
												<form:checkbox tabindex="0" id="productUpdate"
													class="toggle-button__input" path="productUpdate" />
												<label for="productUpdate">
													<div class="toggle-button__switch"></div>
												</label>
											</div>
                                        </li>
                                        <li class="consent-management-list__item is-expanded"
                                            data-binding="expand-accordion-item">
                                            <span tabindex="3" class="consent-management-list__title"
                                                aria-controls="panel4" role="tab" aria-selected="true"
                                                aria-expanded="true" data-binding="expand-accordion-trigger">
                                                <label for="productPromo"
                                                    class="consent-management-list__label"><spring:theme code="text.preference.overlay.productPromo" /></label>
                                            </span>
                                            <div id="consent-template-description-3" class="consent-management-list__content" role="tabpanel"
												aria-hidden="false" aria-labelledby="tab1" data-binding="expand-accordion-container style="height: 76px;">
												<div class="consent-management-list__content-inner">
													<p>
														<spring:theme code="text.preference.overlay.productPromo.description" />
													</p>
												</div>
											</div>
                                            <div class="toggle-button">
												<form:checkbox tabindex="0" id="productPromo"
													class="toggle-button__input" path="productPromo" />
												<label for="productPromo">
													<div class="toggle-button__switch"></div>
												</label>
											</div>
                                        </li>
                                    </ul>
                                    <button type="submit" class="btn btn-primary consentsPreferences-submit">Submit</button>
									</form:form>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>