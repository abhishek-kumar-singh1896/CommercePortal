<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	
	<!-- ########################################################################################
	# This template sets the processor-specific PreAuthorization Amount
	############################################################################################# -->
	<xsl:template name="PreAuthAmount">
	    <xsl:param name="xipayMID"/>
	    <xsl:param name="cardType"/>
		<xsl:choose>
			<xsl:when test="contains('GCA_CAD_GCA', $xipayMID)">
				<xsl:value-of select="'1.00'"/>
			</xsl:when>
			<xsl:when test="contains('VI MC', $cardType) and not(contains('GCA_CAD_GCA', $xipayMID))">
				<xsl:value-of select="'0.00'"/>
			</xsl:when>
			<xsl:when test="contains('AX DI', $cardType) and not(contains('GCA_CAD_GCA', $xipayMID))">
				<xsl:value-of select="'1.00'"/>
			</xsl:when>
			<xsl:otherwise><xsl:value-of select="'0.00'"/></xsl:otherwise>
		</xsl:choose>
	</xsl:template>  

	<!-- ########################################################################################
	# This template sets the processor-specific Expiration Date
	############################################################################################# -->
	<xsl:template name="ExpiryDate">
	    <xsl:param name="xipayMID"/>
		<xsl:param name="expMonth"/>
		<xsl:param name="expYear"/>
		<xsl:choose>
			<xsl:when test="string-length($expMonth) = 1"><xsl:value-of select="concat('0', $expMonth)"/></xsl:when>
			<xsl:otherwise><xsl:value-of select="$expMonth"/></xsl:otherwise>
		</xsl:choose>
		<xsl:value-of select="'/'"/>
		<xsl:value-of select="$expYear"/>
	</xsl:template> 

	<!-- ########################################################################################
	# This template sets the processor-specific CVV Response Info-Item Name
	############################################################################################# -->
	<xsl:template name="cvvResponseInfoItem">
		<xsl:value-of select="'TR_CARD_CIDRESPCODE'"/>
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
		<xsl:choose>
			<xsl:when test="$op = '1'"> <!-- Card Validation -->
				
				<xsl:if test="contains('GCA_CAD_GCA',$xipayMID)">
					<InfoItem><key>TR_ECOMM_IND</key><value><xsl:value-of select="'7'"/></value></InfoItem>
				</xsl:if>
			
			</xsl:when>

			<xsl:when test="$op = '2'"> <!-- Full Authorization -->
			
				<xsl:if test="contains('GCA_CAD_GCA',$xipayMID)">
					<InfoItem><key>TR_ECOMM_IND</key><value><xsl:value-of select="'7'"/></value></InfoItem>
				</xsl:if>
				
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
		<xsl:choose>
			<xsl:when test="contains('usd-USD-*', $currency) and contains('VI-MC-AX-DI-*', $cardType)"><xsl:value-of select="'GUS_USD_GUS'"/></xsl:when>
			<xsl:when test="contains('cad-CAD-*', $currency) and contains('VI-MC-AX-DI-*', $cardType)"><xsl:value-of select="'GCA_CAD_GCA'"/></xsl:when>
			<xsl:when test="contains('aud-AUD-*', $currency) and contains('VI-MC-AX-DI-*', $cardType)"><xsl:value-of select="'GAU_AUD_GAU'"/></xsl:when>
			<xsl:when test="contains('nzd-NZD-*', $currency) and contains('VI-MC-AX-DI-*', $cardType)"><xsl:value-of select="'GGL_NZD_GGL'"/></xsl:when>
			
        </xsl:choose>
	</xsl:template> 

	<!-- ########################################################################################
	# This template determines the successful AVS response code by card type
	############################################################################################# -->
	<xsl:template name="SuccessfulAVSCodes">
	    <xsl:param name="xipayMID"/>
	    <xsl:param name="cardType"/>
	    <xsl:choose>
		    <xsl:when test="contains('GUS_USD_GUS',$xipayMID) and contains('VI', $cardType)"><xsl:value-of select="'A;B;C;P;R;S;U;X;Y;Z'"/></xsl:when>
		    <xsl:when test="contains('GUS_USD_GUS',$xipayMID) and contains('MC', $cardType)"><xsl:value-of select="'X;Y;A;W;Z;U;R;E;S'"/></xsl:when>
		    <xsl:when test="contains('GUS_USD_GUS',$xipayMID) and contains('AX', $cardType)"><xsl:value-of select="'Y;A;Z;U;R;S;L;M;O;D;E;F'"/></xsl:when>
		    <xsl:when test="contains('GUS_USD_GUS',$xipayMID) and contains('DI', $cardType)"><xsl:value-of select="'X;Y;A;W;Z;U;R;S'"/></xsl:when>
		    <xsl:when test="contains('GCA_CAD_GCA',$xipayMID) and contains('VI-MC-AX-*', $cardType)"><xsl:value-of select="'A;B;C;D;M;P;Y;Z'"/></xsl:when>
		    <xsl:when test="contains('GCA_CAD_GCA',$xipayMID) and contains('DI', $cardType)"><xsl:value-of select="'X;A;Y;T;Z'"/></xsl:when>
		    <xsl:when test="contains('GAU_AUD_GAU',$xipayMID)"><xsl:value-of select="'A;B;D;M;F;H;L;O;P;T;V;W;Y;Z;3'"/></xsl:when>
			<xsl:when test="contains('GGL_NZD_GGL',$xipayMID)"><xsl:value-of select="'A;B;D;M;F;H;L;O;P;T;V;W;Y;Z;3'"/></xsl:when>
		</xsl:choose>
	</xsl:template> 

	<!-- ########################################################################################
	# This template determines the successful CVV response code by card type
	############################################################################################# -->
	<xsl:template name="SuccessfulCVVCodes">
	    <xsl:param name="xipayMID"/>
	    <xsl:param name="cardType"/>
	    <xsl:choose>
		    <xsl:when test="contains('GUS_USD_GUS',$xipayMID)"><xsl:value-of select="'M;P;U;X'"/></xsl:when>
		    <xsl:when test="contains('GCA_CAD_GCA',$xipayMID)"><xsl:value-of select="'M;P;U;l'"/></xsl:when>
		    <xsl:when test="contains('GAU_AUD_GAU',$xipayMID)"><xsl:value-of select="'M'"/></xsl:when>
			<xsl:when test="contains('GGL_NZD_GGL',$xipayMID)"><xsl:value-of select="'M'"/></xsl:when>
		</xsl:choose>
	</xsl:template>
	
	<!-- ########################################################################################
	# This template determines the authorization response code produced by the processor
	############################################################################################# -->
	<xsl:template name="AuthorizationResponseCode">
	    <xsl:param name="packet"/>
		<xsl:choose>
			<xsl:when test="contains('GUS_USD_GUS',$packet/merchantID)"><xsl:value-of select="$packet/infoItems/InfoItem[key='TR_CARD_RESPCODE']/value"/></xsl:when>
			<xsl:when test="contains('GCA_CAD_GCA',$packet/merchantID)"><xsl:value-of select="$packet/ResponseCode"/></xsl:when>
			<xsl:when test="contains('GAU_AUD_GAU',$packet/merchantID)"><xsl:value-of select="$packet/ResponseCode"/></xsl:when>
			<xsl:when test="contains('GGL_NZD_GGL',$packet/merchantID)"><xsl:value-of select="$packet/ResponseCode"/></xsl:when>			
		</xsl:choose>
	</xsl:template> 

</xsl:stylesheet>