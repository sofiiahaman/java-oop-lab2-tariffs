<?xml version="1.0" encoding="UTF-8"?>
<!--
  Transformation: flat list of <Tariff> elements is split into sublists by operator.
  Group element name = value of OperatorName (Kyivstar, Vodafone, lifecell).
  Grouping using the Muenchian method (XSLT 1.0).
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>
    <xsl:strip-space elements="*"/>

    <xsl:key name="by-operator" match="Tariff" use="OperatorName"/>

    <xsl:template match="/Tariffs">
        <Tariffs>
            <xsl:for-each select="Tariff[generate-id() = generate-id(key('by-operator', OperatorName)[1])]">
                <!-- case-insensitive and locale-independent -->
                <xsl:sort select="translate(OperatorName, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')"/>
                <xsl:element name="{OperatorName}">
                    <xsl:for-each select="key('by-operator', OperatorName)">
                        <xsl:sort select="Payroll" data-type="number"/>
                        <xsl:apply-templates select="." mode="clean"/>
                    </xsl:for-each>
                </xsl:element>
            </xsl:for-each>
        </Tariffs>
    </xsl:template>

    <!-- Copies an element without redundant namespace declarations (xmlns:xsi) -->
    <xsl:template match="*" mode="clean">
        <xsl:element name="{local-name()}">
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates select="node()" mode="clean"/>
        </xsl:element>
    </xsl:template>
</xsl:stylesheet>