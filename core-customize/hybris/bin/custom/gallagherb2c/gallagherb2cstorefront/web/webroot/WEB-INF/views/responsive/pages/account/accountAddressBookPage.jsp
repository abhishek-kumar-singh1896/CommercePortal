<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="breadcrumb"
	tagdir="/WEB-INF/tags/responsive/nav/breadcrumb"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<div class="main__inner-wrapper">
	<div class="global-alerts"></div>
	<div class="account-section">
		<div class="yCmsContentSlot account-section-content">
			<div class="container">
				<div class="row">
					<div class="col-12">
						<div class="breadcrumb-out">
							<c:if test="${fn:length(breadcrumbs) > 0}">
								<nav aria-label="breadcrumb">
									<breadcrumb:breadcrumb breadcrumbs="${breadcrumbs}" />
								</nav>
							</c:if>
						</div>
					</div>
				</div>
				<div class="">
					<h1 class="primary-title">
						<spring:theme code="text.account.addressBook" />
					</h1>
					<ycommerce:testId code="addressBook_addNewAddress_button">
						<div class="account-section-header-add pull-right">
							<a href="add-address" class="btn btn-primary"> <spring:theme
									code="text.account.addressBook.addAddress" />
							</a>
						</div>
					</ycommerce:testId>
					<div class="account-section-content mb-5 mt-5">

						<div class="account-addressbook account-list">
							<c:if test="${empty addressData}">
								<div class="account-section-content content-empty">
									<spring:theme code="text.account.addressBook.noSavedAddresses" />
								</div>
							</c:if>

							<c:if test="${not empty addressData}">
								<div class="account-cards card-select">
									<div class="row">
										<c:forEach items="${addressData}" var="address">
											<div class="col-xs-12 col-sm-6 col-md-4 col-lg-3 card">
												<ul class="pull-left">
													<li><strong>${fn:escapeXml(address.title)}&nbsp;${fn:escapeXml(address.firstName)}&nbsp;${fn:escapeXml(address.lastName)}
															<c:if test="${address.defaultAddress}">
																<span class="default-text"> (<spring:theme
																		code="text.default" />)
																</span>
															</c:if>
													</strong></li>
													<li>${fn:escapeXml(address.line1)}</li>
													<c:if test="${not empty fn:escapeXml(address.line2)}">
														<li>${fn:escapeXml(address.line2)}</li>
													</c:if>
													<li>${fn:escapeXml(address.town)}&nbsp;${fn:escapeXml(address.region.name)}</li>
													<li>
														${fn:escapeXml(address.country.name)}&nbsp;${fn:escapeXml(address.postalCode)}</li>
													<li>${fn:escapeXml(address.phone)}</li>
												</ul>

												<div class="clearfix">
													<c:if test="${not address.defaultAddress}">
														<ycommerce:testId code="addressBook_isDefault_button">
															<a class="account-set-default-address"
																href="set-default-address/${fn:escapeXml(ycommerce:encodeUrl(address.id))}">
																<spring:theme code="text.setDefault" />
															</a>
														</ycommerce:testId>
													</c:if>
													<div class="account-cards-actions pull-left">
														<ycommerce:testId code="addressBook_editAddress_button">
															<a class="action-links"
																href="edit-address/${fn:escapeXml(ycommerce:encodeUrl(address.id))}">
																<span class="glyphicon glyphicon-pencil"></span>
															</a>
														</ycommerce:testId>
														<ycommerce:testId code="addressBook_removeAddress_button">
															<a href="#"
																class="action-links removeAddressFromBookButton"
																data-address-id="${fn:escapeXml(address.id)}"
																data-popup-title="<spring:theme code="text.address.delete.popup.title" />">
																<span class="glyphicon glyphicon-remove"></span>
															</a>
														</ycommerce:testId>
													</div>
												</div>
											</div>
										</c:forEach>
									</div>

									<c:forEach items="${addressData}" var="address">
										<div class="display-none">
											<div
												id="popup_confirm_address_removal_${fn:escapeXml(address.id)}"
												class="account-address-removal-popup">
												<div class="addressItem">
													<spring:theme code="text.address.remove.following" />

													<div class="address">
														<strong> 
														<c:if test="${not empty fn:escapeXml(address.title)}">
														${fn:escapeXml(address.title)}&nbsp;
														</c:if>
															${fn:escapeXml(address.firstName)}&nbsp;
															${fn:escapeXml(address.lastName)} </strong> <br>
														${fn:escapeXml(address.line1)}&nbsp;
														${fn:escapeXml(address.line2)} <br>
														${fn:escapeXml(address.town)}&nbsp;
														<c:if test="${not empty address.region.name }">
						           						 ${fn:escapeXml(address.region.name)}&nbsp;
						        						</c:if>
														<br> ${fn:escapeXml(address.country.name)}&nbsp;
														${fn:escapeXml(address.postalCode)} <br />


														${fn:escapeXml(address.phone)}
													</div>

													<div class="modal-actions">
														<div class="row">
															<ycommerce:testId code="addressRemove_delete_button">
																<div class="col-xs-12 col-sm-6 col-sm-push-6">
																	<a class="btn btn-primary btn-block"
																		data-address-id="${fn:escapeXml(address.id)}"
																		href="remove-address/${fn:escapeXml(ycommerce:encodeUrl(address.id))}">
																		<spring:theme code="text.address.delete" />
																	</a>
																</div>
															</ycommerce:testId>
															<div class="col-xs-12 col-sm-6 col-sm-pull-6">
																<a class="btn btn-default btn-block closeColorBox"
																	data-address-id="${fn:escapeXml(address.id)}"> <spring:theme
																		code="text.button.cancel" />
																</a>
															</div>
														</div>
													</div>
												</div>
											</div>
										</div>
									</c:forEach>
								</div>
							</c:if>
						</div>

					</div>
				</div>
			</div>
		</div>
	</div>
</div>

