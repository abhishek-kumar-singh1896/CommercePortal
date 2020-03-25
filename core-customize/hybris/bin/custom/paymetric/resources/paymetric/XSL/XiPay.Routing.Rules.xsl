<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!-- ########################################################################################
	# Rodan and Fields - Worldpay Cartridge
	############################################################################################# -->


	<!-- ########################################################################################
	# This template sets the processor-specific PreAuthorization Amount
	############################################################################################# -->
	<xsl:template name="PreAuthAmount">
	    <xsl:param name="xipayMID"/>
	    <xsl:param name="cardType"/>		
		<xsl:choose>
			<xsl:when test="contains('GUS_USD_GUS GAU_AUD_GAU', $xipayMID) and contains('AX-DI-ax-di', $cardType)">
				<xsl:value-of select="'1.00'"/>
			</xsl:when>
			<xsl:when test="contains('GCA_CAD_GCA', $xipayMID)">
				<xsl:value-of select="'1.00'"/>
			</xsl:when>
			<xsl:otherwise><xsl:value-of select="'0.00'"/></xsl:otherwise>
		</xsl:choose>
	</xsl:template> 

	<!-- ########################################################################################
	# This template formats the processor-specific Expiration Date
	############################################################################################# -->
	<xsl:template name="ExpiryDate">
	    <xsl:param name="xipayMID"/>
		<xsl:param name="expMonth"/>
		<xsl:param name="expYear"/>
		<xsl:choose>
			<xsl:when test="string-length($expMonth) = 1"><xsl:value-of select="concat('0', $expMonth)"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="$expMonth"/></xsl:otherwise>
		</xsl:choose>
		<xsl:value-of select="number($expYear) - 2000"/>
	</xsl:template> 

	<!-- ########################################################################################
	# This template sets the processor-specific CVV Response Info-Item Name
	############################################################################################# -->
	<xsl:template name="cvvResponseInfoItem">
	 	<xsl:param name="xipayMID"/>
	 	<xsl:choose>
	 		<xsl:value-of select="'TR_CARD_CIDRESPCODE'"/>
	 	</xsl:choose>
	</xsl:template>

	<!-- ########################################################################################
	# This template adds/sets any processor-specific header data
	############################################################################################# -->
	<xsl:template name="CustomHeader">
	    <xsl:param name="xipayMID"/>
	    <xsl:param name="cardType"/>
	    <xsl:param name="op"/>
	    <xsl:param name="nodes"/>
		
		<xsl:choose>
			<xsl:when test="$op = '1'"> <!-- Card Validation -->
				<xsl:if test="$nodes/entry[string[1]='merchantRefenceNumber']">
					<PONumber>
						<xsl:value-of select="$nodes/entry[string[1]='merchantRefenceNumber']/string[2]"/>
					</PONumber>
					<salesDocNumber>
						<xsl:value-of select="$nodes/entry[string[1]='merchantRefenceNumber']/string[2]"/>
					</salesDocNumber>
				</xsl:if>

				<xsl:variable name="orderSource">
					<xsl:value-of select="$nodes/entry[string[1]='orderSource']/string[2]" />
				</xsl:variable>
				<cardDataSource>
					<xsl:choose>
						<xsl:when test="$orderSource = 'telephone'"><xsl:value-of select="'M'" /></xsl:when>
						<xsl:otherwise><xsl:value-of select="'E'" /></xsl:otherwise>
					</xsl:choose>
				</cardDataSource>
			</xsl:when>

			<xsl:when test="$op = '2'"> <!-- Full Authorization -->
				<PONumber>
					<xsl:value-of select="$nodes/customFields/reportingGroup"/>
				</PONumber>
				<salesDocNumber>
					<xsl:value-of select="$nodes/customFields/orderNumber"/>
				</salesDocNumber>
				<bankBatchID>
					<xsl:value-of select="$nodes/customFields/customerCid"/>
				</bankBatchID>

				<xsl:variable name="orderSource">
					<xsl:choose>
						<xsl:when test="$nodes/customFields/orderSource"><xsl:value-of select="$nodes/customFields/orderSource" /></xsl:when>
						<xsl:otherwise><xsl:value-of select="'ecommerce'" /></xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<cardDataSource>
					<xsl:choose>
						<xsl:when test="$orderSource = 'ecommerce'"><xsl:value-of select="'E'" /></xsl:when>
						<xsl:otherwise><xsl:value-of select="'M'" /></xsl:otherwise>
					</xsl:choose>
				</cardDataSource>
			</xsl:when>

			<xsl:when test="$op = '3'"> <!-- Capture -->
			</xsl:when>

			<xsl:when test="$op = '4'"> <!-- Refunds -->
				<PONumber>
					<xsl:value-of select="$nodes/customFields/reportingGroup"/>
				</PONumber>
				<salesDocNumber>
					<xsl:value-of select="$nodes/customFields/orderNumber"/>
				</salesDocNumber>
				<bankBatchID>
					<xsl:value-of select="$nodes/customFields/customerCid"/>
				</bankBatchID>

				<xsl:variable name="orderSource">
					<xsl:choose>
						<xsl:when test="$nodes/customFields/orderSource"><xsl:value-of select="$nodes/customFields/orderSource" /></xsl:when>
						<xsl:otherwise><xsl:value-of select="'ecommerce'" /></xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<cardDataSource>
					<xsl:choose>
						<xsl:when test="$orderSource = 'ecommerce'"><xsl:value-of select="'E'" /></xsl:when>
						<xsl:otherwise><xsl:value-of select="'M'" /></xsl:otherwise>
					</xsl:choose>
				</cardDataSource>
			</xsl:when>

			<xsl:when test="$op = '5'"> <!-- Voids -->
			</xsl:when>

			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>

	<!-- ########################################################################################
	# This template adds/sets any processor-specific info-item data
	############################################################################################# -->
	<xsl:template name="CustomInfoItems">
	    <xsl:param name="xipayMID"/>
	    <xsl:param name="cardType"/>
	    <xsl:param name="op"/>
	    <xsl:param name="nodes"/>
		
		<!-- ########################################################################################
		# Handle CVV Present Indicator for FDMS North
		############################################################################################# -->
		<xsl:choose>
			<xsl:when test="$op = '1'"> <!-- Card Validation -->
				<xsl:if test="string-length($nodes/entry[string[1]='card_cvNumber']/string[2]) = 0">
					<InfoItem><key>TR_CARD_CIDIND</key><value><xsl:value-of select="'0'"/></value></InfoItem>
				</xsl:if>
				<xsl:if test="$xipayMID = 'GCA_CAD_GCA'">
					<InfoItem><key>TR_ECOMM_IND</key><value><xsl:value-of select="'7'"/></value></InfoItem>
				</xsl:if>
			</xsl:when>

			<xsl:when test="$op = '2'"> <!-- Full Authorization -->
				<xsl:if test="string-length($nodes/paymentInfo/creditCard/cardCVV) = 0">
					<InfoItem><key>TR_CARD_CIDIND</key><value><xsl:value-of select="'0'"/></value></InfoItem>
				</xsl:if>
				<xsl:if test="$xipayMID = 'GCA_CAD_GCA'">
					<InfoItem><key>TR_ECOMM_IND</key><value><xsl:value-of select="'7'"/></value></InfoItem>
				</xsl:if>
			</xsl:when>

			<xsl:when test="$op = '3'"> <!-- Capture -->
			</xsl:when>

			<xsl:when test="$op = '4'"> <!-- Refunds -->
				<xsl:if test="string-length($nodes/card/cv2Number) = 0">
					<InfoItem><key>TR_CARD_CIDIND</key><value><xsl:value-of select="'0'"/></value></InfoItem>
				</xsl:if>
				<xsl:if test="$xipayMID = 'GCA_CAD_GCA'">
					<InfoItem><key>TR_ECOMM_IND</key><value><xsl:value-of select="'7'"/></value></InfoItem>
				</xsl:if>
			</xsl:when>

			<xsl:when test="$op = '5'"> <!-- Voids -->
			</xsl:when>

			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>

		<xsl:choose>
			<xsl:when test="$op = '1'"> <!-- Card Validation -->
				<InfoItem><key>HD_BILLTO_ADDR</key><value><xsl:value-of select="$nodes/entry[string[1]='billTo_street1']/string[2]"/></value></InfoItem>
				<InfoItem><key>HD_BILLTO_CITY</key><value><xsl:value-of select="$nodes/entry[string[1]='billTo_city']/string[2]"/></value></InfoItem>
				<InfoItem><key>HD_BILLTO_COUNTRY</key><value><xsl:value-of select="$nodes/entry[string[1]='billTo_country']/string[2]"/></value></InfoItem>
				<InfoItem><key>HD_BILLTO_STATE</key><value><xsl:value-of select="$nodes/entry[string[1]='billTo_state']/string[2]"/></value></InfoItem>
				<InfoItem><key>HD_BILLTO_ZIP</key><value><xsl:value-of select="$nodes/entry[string[1]='billTo_postalCode']/string[2]"/></value></InfoItem>

				<InfoItem><key>HD_BUYER_CNTCTEMAIL</key><value><xsl:value-of select="$nodes/entry[string[1]='billTo_email']/string[2]"/></value></InfoItem>
				<InfoItem><key>HD_BUYER_FNAME</key><value><xsl:value-of select="$nodes/entry[string[1]='billTo_firstName']/string[2]"/></value></InfoItem>
				<InfoItem><key>HD_BUYER_LNAME</key><value><xsl:value-of select="$nodes/entry[string[1]='billTo_lastName']/string[2]"/></value></InfoItem>
				
				<!-- 3DS2 ELEMENTS FOR WORLDPAY PROCESSOR 
					<InfoItem><key>TR_ECOMM_IND</key><value><xsl:value-of select="$nodes/entry[string[1]='card_eciFlag']/string[2]"/></value></InfoItem>
					<InfoItem><key>TR_ECOMM_PARESSTATUS</key><value><xsl:value-of select="$nodes/entry[string[1]='card_parEsStatus']/string[2]"/></value></InfoItem>
					<InfoItem><key>TR_ECOMM_VBVCAVV</key><value><xsl:value-of select="$nodes/entry[string[1]='card_cavv']/string[2]"/></value></InfoItem>
					<InfoItem><key>TR_ECOMM_VBVXID</key><value><xsl:value-of select="$nodes/entry[string[1]='card_dstranid']/string[2]"/></value></InfoItem> -->
					
			</xsl:when>

			<xsl:when test="$op = '2'"> <!-- Full Authorization -->
				<xsl:variable name="orderSource">
					<xsl:choose>
						<xsl:when test="$nodes/customFields/orderSource"><xsl:value-of select="$nodes/customFields/orderSource" /></xsl:when>
						<xsl:otherwise><xsl:value-of select="'ecommerce'" /></xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				
				<xsl:if test="contains('VI-MC-DI-AX-*', $cardType)">
					<InfoItem><key>HD_BILLTO_ADDR</key><value><xsl:value-of select="$nodes/paymentInfo/billingInfo/cardHolderAddress1"/></value></InfoItem>
					<InfoItem><key>HD_BILLTO_CITY</key><value><xsl:value-of select="$nodes/paymentInfo/billingInfo/cardHolderCity"/></value></InfoItem>
					<InfoItem><key>HD_BILLTO_CNTCTPHONE</key><value><xsl:value-of select="$nodes/shippingInfo/phoneNumber"/></value></InfoItem>
					<InfoItem><key>HD_BILLTO_COUNTRY</key><value><xsl:value-of select="$nodes/paymentInfo/billingInfo/cardHolderCountry"/></value></InfoItem>
					<InfoItem><key>HD_BILLTO_STATE</key><value><xsl:value-of select="$nodes/paymentInfo/billingInfo/cardHolderState"/></value></InfoItem>
					<InfoItem><key>HD_BILLTO_ZIP</key><value><xsl:value-of select="$nodes/paymentInfo/billingInfo/cardHolderZip"/></value></InfoItem>
					<InfoItem><key>HD_BUYER_CNTCTEMAIL</key><value><xsl:value-of select="$nodes/customerNumber"/></value></InfoItem>
					<InfoItem><key>HD_BUYER_FNAME</key><value><xsl:value-of select="$nodes/paymentInfo/billingInfo/cardHolderName1"/></value></InfoItem>
					<InfoItem><key>HD_BUYER_LNAME</key><value><xsl:value-of select="$nodes/paymentInfo/billingInfo/cardHolderName2"/></value></InfoItem>
				</xsl:if>

				<!--<xsl:variable name="varMsgType">
					<xsl:choose>
						<xsl:when test="$orderSource = 'telephone'"><xsl:value-of select="'MUSE'" /></xsl:when>
						<xsl:when test="$orderSource = 'recurring'"><xsl:value-of select="'MREC'" /></xsl:when>
						<xsl:when test="$orderSource = 'ecommerce'"><xsl:value-of select="'CUSE'" /></xsl:when>
						<xsl:otherwise><xsl:value-of select="$nodes/paymentInfo/creditCard/TR_TRANS_MSGTYPE" /></xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<InfoItem><key>TR_TRANS_MSGTYPE</key><value><xsl:value-of select="$varMsgType"/></value></InfoItem> -->

				<xsl:if test="$nodes/fraudEnabled = 'true' and contains('CUSE-MUSE', $varMsgType)">
					<!-- ########################################################################################
					# 3 - Post-authorization fraud. The authorization attempt is made first and then the fraud 
					# service call will always be performed.  This means the fraud service will be called if the 
					# authorization is declined or successful.
					############################################################################################# -->
					<InfoItem><key>TR_FRAUD_SERVICE</key><value><xsl:value-of select="'3'"/></value></InfoItem>
					<InfoItem><key>TR_FRAUD_COOKIES</key><value><xsl:value-of select="$nodes/deviceFingerprint"/></value></InfoItem>
					<InfoItem><key>TR_FRAUD_PROMOCODE</key><value><xsl:value-of select="$nodes/promotions/promotion/code"/></value></InfoItem>
					<InfoItem><key>TR_FRAUD_PROMOAMT</key><value><xsl:value-of select="$nodes/promotions/promotion/amount"/></value></InfoItem>
					<InfoItem><key>TR_FRAUD_MERCHDATA1</key><value><xsl:value-of select="$nodes/customFields/sponsorId"/></value></InfoItem>
					<InfoItem><key>TR_FRAUD_MERCHDATA2</key><value><xsl:value-of select="$nodes/customFields/enrollmentDate"/></value></InfoItem>
					<InfoItem><key>TR_FRAUD_MERCHDATA3</key><value><xsl:value-of select="$nodes/customFields/neverBounceResultCode"/></value></InfoItem>
					<InfoItem><key>TR_FRAUD_MERCHDATA4</key><value><xsl:value-of select="'N/A'"/></value></InfoItem>
					<InfoItem><key>TR_FRAUD_MERCHDATA5</key><value><xsl:value-of select="$xipayMID"/></value></InfoItem>

					<InfoItem><key>HD_BILLTO_ADDR</key><value><xsl:value-of select="$nodes/paymentInfo/billingInfo/cardHolderAddress1"/></value></InfoItem>
					<InfoItem><key>HD_BILLTO_CITY</key><value><xsl:value-of select="$nodes/paymentInfo/billingInfo/cardHolderCity"/></value></InfoItem>
					<InfoItem><key>HD_BILLTO_CNTCTPHONE</key><value><xsl:value-of select="$nodes/shippingInfo/phoneNumber"/></value></InfoItem>
					<InfoItem><key>HD_BILLTO_COUNTRY</key><value><xsl:value-of select="$nodes/paymentInfo/billingInfo/cardHolderCountry"/></value></InfoItem>
					<InfoItem><key>HD_BILLTO_STATE</key><value><xsl:value-of select="$nodes/paymentInfo/billingInfo/cardHolderState"/></value></InfoItem>
					<InfoItem><key>HD_BILLTO_ZIP</key><value><xsl:value-of select="$nodes/paymentInfo/billingInfo/cardHolderZip"/></value></InfoItem>

					<InfoItem><key>HD_BUYER_DEVICEID</key><value><xsl:value-of select="$nodes/deviceFingerprint"/></value></InfoItem>
					<InfoItem><key>HD_BUYER_DEVICETOKEN</key><value><xsl:value-of select="$nodes/deviceFingerprint"/></value></InfoItem>
					<InfoItem><key>HD_BUYER_CNTCTEMAIL</key><value><xsl:value-of select="$nodes/customFields/contactEmail"/></value></InfoItem>
					<InfoItem><key>HD_BUYER_IPADDRESS</key><value><xsl:value-of select="$nodes/shippingInfo/ipAddress"/></value></InfoItem>
					<InfoItem><key>HD_BUYER_FNAME</key><value><xsl:value-of select="$nodes/paymentInfo/billingInfo/cardHolderName1"/></value></InfoItem>
					<InfoItem><key>HD_BUYER_LNAME</key><value><xsl:value-of select="$nodes/paymentInfo/billingInfo/cardHolderName2"/></value></InfoItem>

					<InfoItem><key>HD_ORDER_CUSTREFNUM</key><value><xsl:value-of select="$nodes/customFields/orderNumber"/></value></InfoItem>

					<InfoItem><key>HD_MERCH_SELLERID</key><value><xsl:value-of select="$nodes/customFields/customerCid"/></value></InfoItem>
					<InfoItem><key>HD_MERCH_STORENUM</key><value><xsl:value-of select="$nodes/customFields/orderTypeId"/></value></InfoItem>

					<InfoItem><key>HD_SHIPTO_ADDR</key><value><xsl:value-of select="$nodes/shippingInfo/street1"/></value></InfoItem>
					<InfoItem><key>HD_SHIPTO_CITY</key><value><xsl:value-of select="$nodes/shippingInfo/city"/></value></InfoItem>
					<InfoItem><key>HD_SHIPTO_CNTRY</key><value><xsl:value-of select="$nodes/shippingInfo/country"/></value></InfoItem>
					<InfoItem><key>HD_SHIPTO_CNTCTEMAIL</key><value><xsl:value-of select="$nodes/shippingInfo/state"/></value></InfoItem>
					<InfoItem><key>HD_SHIPTO_NAME</key><value><xsl:value-of select="concat($nodes/shippingInfo/lastName, ' ', $nodes/shippingInfo/firstName)"/></value></InfoItem>
					<InfoItem><key>HD_SHIPTO_CNTCTPHONE</key><value><xsl:value-of select="$nodes/shippingInfo/phoneNumber"/></value></InfoItem>
					<InfoItem><key>HD_SHIPTO_ZIP</key><value><xsl:value-of select="$nodes/shippingInfo/postalCode"/></value></InfoItem>
					<InfoItem><key>HD_SHIPTO_STATE</key><value><xsl:value-of select="$nodes/shippingInfo/state"/></value></InfoItem>

					<InfoItem><key>HD_SHIPPING_METHOD</key><value><xsl:value-of select="$nodes/shippingInfo/shippingMethod"/></value></InfoItem>
					<InfoItem><key>HD_SHIPPING_COST</key><value><xsl:value-of select="$nodes/shippingInfo/shippingCost"/></value></InfoItem>
				</xsl:if>
			</xsl:when>

			<xsl:when test="$op = '3'"> <!-- Capture -->
			</xsl:when>

			<xsl:when test="$op = '4'"> <!-- Refunds -->
				<InfoItem><key>TR_TRANS_REFID</key><value><xsl:value-of select="$nodes/subscriptionID"/></value></InfoItem>
			</xsl:when>

			<xsl:when test="$op = '5'"> <!-- Voids -->
			</xsl:when>

			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- ########################################################################################
	# This template adds/sets any processor-specific Line Item info-items data
	############################################################################################# -->
	<xsl:template name="CustomLineInfoItems">
	    <xsl:param name="xipayMID"/>
	    <xsl:param name="cardType"/>
	    <xsl:param name="op"/>
	    <xsl:param name="nodes"/>

		<xsl:choose>
			<xsl:when test="$op = '1'"> <!-- Card Validation -->
			</xsl:when>

			<xsl:when test="$op = '2'"> <!-- Full Authorization -->
			</xsl:when>

			<xsl:when test="$op = '3'"> <!-- Capture -->
			</xsl:when>

			<xsl:when test="$op = '4'"> <!-- Refunds -->
			</xsl:when>

			<xsl:when test="$op = '5'"> <!-- Voids -->
			</xsl:when>

			<xsl:otherwise>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	
	

	<!-- ########################################################################################
	# This template determines the XiPay Routing MID
	############################################################################################# -->
	<xsl:template name="XiPayMID">
	    <xsl:param name="cardType"/>
	    <xsl:param name="currency"/>
	    <xsl:param name="country"/>
	    <xsl:param name="business"/>
	    <xsl:param name="nodes"/>
		<xsl:choose>
			<xsl:when test="contains('usd-USD-*', $currency) and contains('VI-MC-AX-DI-*', $cardType)"><xsl:value-of select="'GUS_USD_GUS'"/></xsl:when>
			<xsl:when test="contains('cad-CAD-*', $currency) and contains('VI-MC-AX-DI-*', $cardType)"><xsl:value-of select="'GCA_CAD_GCA'"/></xsl:when>
			<xsl:when test="contains('aud-AUD-*', $currency) and contains('VI-MC-AX-DI-*', $cardType)"><xsl:value-of select="'GAU_AUD_GAU'"/></xsl:when>
		</xsl:choose>

		<!-- ########################################################################################
		# Determine the Cronjob indicator
		############################################################################################# 
		<xsl:variable name="varCronJob">
			<xsl:choose>
				<xsl:when test="$nodes/cronJob">
					<xsl:value-of select="$nodes/cronJob" />
				</xsl:when>
				<xsl:when test="$nodes/entry[string[1]='cronJob']">
					<xsl:value-of select="$nodes/entry[string[1]='cronJob']/string[2]" />
				</xsl:when>
				<xsl:otherwise><xsl:value-of select="'false'" /></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="orderSource">
			<xsl:choose>
				<xsl:when test="$nodes/entry[string[1]='orderSource']/string[2]"><xsl:value-of select="$nodes/entry[string[1]='orderSource']/string[2]" /></xsl:when>
				<xsl:when test="$nodes/customFields/orderSource"><xsl:value-of select="$nodes/customFields/orderSource" /></xsl:when>
				<xsl:when test="$varCronJob = 'true'"><xsl:value-of select="'recurring'" /></xsl:when>
				<xsl:otherwise><xsl:value-of select="'ecommerce'" /></xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:choose>
			<xsl:when test="contains('VI-MC-AX-*', $cardType)">
				<xsl:value-of select="'RF_JPY_RT_VMA'"/>
			</xsl:when>
			<xsl:when test="contains('JC-*', $cardType)">
				<xsl:value-of select="'RF_JPY_RT_JCB'"/>
			</xsl:when>
		</xsl:choose>
	</xsl:template> -->

	<!-- ########################################################################################
	# This template determines the successful AVS response code by card type
	############################################################################################# -->
	<xsl:template name="SuccessfulAVSCodes">
	    <xsl:param name="xipayMID"/>
	    <xsl:param name="cardType"/>
		<xsl:when test="$xipayMID = 'GUS_USD_GUS' and $cardType = 'VI'"><xsl:value-of select="'A;B;D;F;G;M;P;Y;Z'"/></xsl:when>
		<xsl:when test="$xipayMID = 'GUS_USD_GUS' and $cardType = 'MC'"><xsl:value-of select="''X;Y;A;W;Z;U;S'"/></xsl:when>
		<xsl:when test="$xipayMID = 'GUS_USD_GUS' and $cardType = 'DI'"><xsl:value-of select="'X;Y;A;W;Z;U;S;G'"/></xsl:when>
		<xsl:when test="$xipayMID = 'GUS_USD_GUS' and $cardType = 'AX'"><xsl:value-of select="'Y;A;Z;U;R;S;L;M;O;K;E;F'"/></xsl:when>
		<xsl:when test="$xipayMID = 'GCA_CAD_GCA' and contains('VI-MC-AX-*', $cardType)"><xsl:value-of select="'A;B;C;D;M;P;Y;Z'"/></xsl:when>
		<xsl:when test="$xipayMID = 'GCA_CAD_GCA' and $cardType = 'DI'"><xsl:value-of select="'X;A;Y;T;Z'"/></xsl:when>
		<xsl:when test="$xipayMID = 'GAU_AUD_GAU'"><xsl:value-of select="'A;B;D;M;F;H;L;O;P;T;V;W;Y;Z;3'"/></xsl:when>
	</xsl:template> 

	<!-- ########################################################################################
	# This template determines the successful CVV response code by card type
	############################################################################################# -->
	<xsl:template name="SuccessfulCVVCodes">
	    <xsl:param name="xipayMID"/>
	    <xsl:param name="cardType"/>
	    <xsl:when test="$xipayMID = 'GUS_USD_GUS'"><xsl:value-of select="'M'"/></xsl:when>
		<xsl:when test="$xipayMID = 'GCA_CAD_GCA'"><xsl:value-of select="'M'"/></xsl:when>
		<xsl:when test="$xipayMID = 'GAU_AUD_GAU'"><xsl:value-of select="'M'"/></xsl:when>
	</xsl:template> 

	<!-- ########################################################################################
	# This template determines the authorization response code produced by the processor
	############################################################################################# -->
	<xsl:template name="AuthorizationResponseCode">
	    <xsl:param name="packet"/>

		<xsl:choose>
			<xsl:when test="contains('VI-MC-AX-*', $packet/cardType)"><xsl:value-of select="$packet/message"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="$packet/responseCode"/></xsl:otherwise>
		</xsl:choose>

	</xsl:template> 

</xsl:stylesheet>
<!-- Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="ToXiPay" userelativepaths="yes" externalpreview="no" url="..\XML\1.1-CardValidation-HashMap.xml" htmlbaseurl="" outputurl="" processortype="saxon8" useresolver="yes" profilemode="2" profiledepth="" profilelength=""
		          urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" validateoutput="no" validator="internal"
		          customvalidator="">
			<advancedProp name="bSchemaAware" value="true"/>
			<advancedProp name="xsltVersion" value="2.0"/>
			<advancedProp name="schemaCache" value="||"/>
			<advancedProp name="iWhitespace" value="0"/>
			<advancedProp name="bWarnings" value="true"/>
			<advancedProp name="bXml11" value="false"/>
			<advancedProp name="bUseDTD" value="false"/>
			<advancedProp name="bXsltOneIsOkay" value="true"/>
			<advancedProp name="bTinyTree" value="true"/>
			<advancedProp name="bGenerateByteCode" value="true"/>
			<advancedProp name="bExtensions" value="true"/>
			<advancedProp name="iValidation" value="0"/>
			<advancedProp name="iErrorHandling" value="fatal"/>
			<advancedProp name="sInitialTemplate" value=""/>
			<advancedProp name="sInitialMode" value=""/>
		</scenario>
	</scenarios>
	<MapperMetaTag>
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="..\XSD\XiPay30WS.xsd" destSchemaRoot="ITransactionHeader" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no">
			<SourceSchema srcSchemaPath="..\XML\1.1-CardValidation-HashMap.xml" srcSchemaRoot="map" AssociatedInstance="" loaderFunction="document" loaderFunctionUsesURI="no"/>
		</MapperInfo>
		<MapperBlockPosition>
			<template match="/">
				<block path="ITransactionHeader/cardExpirationDate/xsl:value-of[1]" x="714" y="219"/>
				<block path="ITransactionHeader/cardExpirationDate/xsl:value-of[2]" x="754" y="219"/>
				<block path="ITransactionHeader/cardHolderCountry/xsl:value-of" x="38" y="112"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
-->