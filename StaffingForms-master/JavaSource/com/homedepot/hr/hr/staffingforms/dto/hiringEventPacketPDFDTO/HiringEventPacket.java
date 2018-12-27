//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.11.15 at 10:45:37 AM EST 
//


package com.homedepot.hr.hr.staffingforms.dto.hiringEventPacketPDFDTO;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="hiringEventDetail">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="eventName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="hireEventId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Applicants">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ApplicantDetails" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="interviewDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="phnscrnId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                             &lt;element name="intExtFlg" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "hiringEventDetail",
    "applicants"
})
@XmlRootElement(name = "HiringEventPacket")
public class HiringEventPacket {

    @XmlElement(required = true)
    protected HiringEventPacket.HiringEventDetail hiringEventDetail;
    @XmlElement(name = "Applicants", required = true)
    protected HiringEventPacket.Applicants applicants;

    /**
     * Gets the value of the hiringEventDetail property.
     * 
     * @return
     *     possible object is
     *     {@link HiringEventPacket.HiringEventDetail }
     *     
     */
    public HiringEventPacket.HiringEventDetail getHiringEventDetail() {
        return hiringEventDetail;
    }

    /**
     * Sets the value of the hiringEventDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link HiringEventPacket.HiringEventDetail }
     *     
     */
    public void setHiringEventDetail(HiringEventPacket.HiringEventDetail value) {
        this.hiringEventDetail = value;
    }

    /**
     * Gets the value of the applicants property.
     * 
     * @return
     *     possible object is
     *     {@link HiringEventPacket.Applicants }
     *     
     */
    public HiringEventPacket.Applicants getApplicants() {
        return applicants;
    }

    /**
     * Sets the value of the applicants property.
     * 
     * @param value
     *     allowed object is
     *     {@link HiringEventPacket.Applicants }
     *     
     */
    public void setApplicants(HiringEventPacket.Applicants value) {
        this.applicants = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="ApplicantDetails" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="interviewDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="phnscrnId" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                   &lt;element name="intExtFlg" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "applicantDetails"
    })
    public static class Applicants {

        @XmlElement(name = "ApplicantDetails")
        protected List<HiringEventPacket.Applicants.ApplicantDetails> applicantDetails;

        /**
         * Gets the value of the applicantDetails property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the applicantDetails property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getApplicantDetails().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link HiringEventPacket.Applicants.ApplicantDetails }
         * 
         * 
         */
        public List<HiringEventPacket.Applicants.ApplicantDetails> getApplicantDetails() {
            if (applicantDetails == null) {
                applicantDetails = new ArrayList<HiringEventPacket.Applicants.ApplicantDetails>();
            }
            return this.applicantDetails;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="interviewDateTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="phnscrnId" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *         &lt;element name="intExtFlg" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "interviewDateTime",
            "phnscrnId",
            "intExtFlg"
        })
        public static class ApplicantDetails {

            @XmlElement(required = true)
            protected String interviewDateTime;
            protected int phnscrnId;
            @XmlElement(required = true)
            protected String intExtFlg;

            /**
             * Gets the value of the interviewDateTime property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getInterviewDateTime() {
                return interviewDateTime;
            }

            /**
             * Sets the value of the interviewDateTime property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setInterviewDateTime(String value) {
                this.interviewDateTime = value;
            }

            /**
             * Gets the value of the phnscrnId property.
             * 
             */
            public int getPhnscrnId() {
                return phnscrnId;
            }

            /**
             * Sets the value of the phnscrnId property.
             * 
             */
            public void setPhnscrnId(int value) {
                this.phnscrnId = value;
            }

            /**
             * Gets the value of the intExtFlg property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getIntExtFlg() {
                return intExtFlg;
            }

            /**
             * Sets the value of the intExtFlg property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setIntExtFlg(String value) {
                this.intExtFlg = value;
            }

        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="eventName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="hireEventId" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "eventName",
        "hireEventId"
    })
    public static class HiringEventDetail {

        @XmlElement(required = true)
        protected String eventName;
        protected int hireEventId;

        /**
         * Gets the value of the eventName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEventName() {
            return eventName;
        }

        /**
         * Sets the value of the eventName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEventName(String value) {
            this.eventName = value;
        }

        /**
         * Gets the value of the hireEventId property.
         * 
         */
        public int getHireEventId() {
            return hireEventId;
        }

        /**
         * Sets the value of the hireEventId property.
         * 
         */
        public void setHireEventId(int value) {
            this.hireEventId = value;
        }

    }

}