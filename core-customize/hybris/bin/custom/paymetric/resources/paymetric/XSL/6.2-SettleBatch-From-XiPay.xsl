<?xml version='1.0' ?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
	xmlns:java="http://xml.apache.org/xalan/java" exclude-result-prefixes="java fn">

	<xsl:import href="Hybris.Library.xsl"/>

	<xsl:output method="xml" encoding="UTF-8" indent="yes"/>

	<xsl:template match="/">
		<xsl:variable name="varBatchID" select="/ITransactionHeader-array/ITransactionHeader[1]/batchID"/>
		<SettleBatchResult>
			<merchantTransactionCode>
				<xsl:value-of select="$varBatchID" />
			</merchantTransactionCode>
		    <requestId>
				<xsl:value-of select="$varBatchID" />
			</requestId>
		    <requestToken>
				<xsl:value-of select="$varBatchID" />
			</requestToken>
    		<reconciliationId>
				<xsl:value-of select="$varBatchID" />
			</reconciliationId>

			<xsl:call-template name="setTransactionStatus">
				<xsl:with-param name="statusCode" select="(//statusCode)[last()]"/>
			</xsl:call-template>

			<xiTransactions>
			<xsl:copy-of select="ITransactionHeader-array/ITransactionHeader"/>
			</xiTransactions>
		</SettleBatchResult>
	</xsl:template>

</xsl:stylesheet>
<!-- Stylus Studio meta-information - (c) 2004-2009. Progress Software Corporation. All rights reserved.

<metaInformation>
	<scenarios>
		<scenario default="yes" name="From XiPay" userelativepaths="yes" externalpreview="no" url="..\XML\6.3-SettleBatch-ITransactionHeaderRS.xml" htmlbaseurl="" outputurl="" processortype="saxon8" useresolver="yes" profilemode="0" profiledepth=""
		          profilelength="" urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" validateoutput="no" validator="internal"
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
		<MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="..\XML\6.4-SettleBatch-SettleBatchResult.xml" destSchemaRoot="ScheduleBatchResult" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no">
			<SourceSchema srcSchemaPath="..\XML\6.3-SettleBatch-ITransactionHeaderRS.xml" srcSchemaRoot="ITransactionHeader-array" AssociatedInstance="" loaderFunction="document" loaderFunctionUsesURI="no"/>
		</MapperInfo>
		<MapperBlockPosition>
			<template match="/">
				<block path="ScheduleBatchResult/merchantTransactionCode/" x="124" y="50"/>
			</template>
		</MapperBlockPosition>
		<TemplateContext></TemplateContext>
		<MapperFilter side="source"></MapperFilter>
	</MapperMetaTag>
</metaInformation>
-->