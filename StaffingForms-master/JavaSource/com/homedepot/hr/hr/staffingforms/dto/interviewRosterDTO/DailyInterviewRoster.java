//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.11.19 at 02:35:46 PM EST 
//


package com.homedepot.hr.hr.staffingforms.dto.interviewRosterDTO;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element name="HiringEventName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Location" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Address" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="City" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="StateCd" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ZipCd" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         &lt;element name="PrintedDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Interview" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="candidate" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="HiringStore" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="HiringStoreDeptNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="HiringStoreJobCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="RSCPhoneScreenNo" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                             &lt;element name="CandidateName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="SSN" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="CandidatePhNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="CandidateEmail" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="RequisitionNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="StoreNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="DepartmentNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="JobCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="Date" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="Time" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="TimeSlot" type="{http://www.w3.org/2001/XMLSchema}string" />
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
    "hiringEventName",
    "location",
    "address",
    "city",
    "stateCd",
    "zipCd",
    "printedDate",
    "interview"
})
@XmlRootElement(name = "DailyInterviewRoster")
public class DailyInterviewRoster {

    @XmlElement(name = "HiringEventName", required = true)
    protected String hiringEventName;
    @XmlElement(name = "Location", required = true)
    protected String location;
    @XmlElement(name = "Address", required = true)
    protected String address;
    @XmlElement(name = "City", required = true)
    protected String city;
    @XmlElement(name = "StateCd", required = true)
    protected String stateCd;
    @XmlElement(name = "ZipCd")
    protected String zipCd;
    @XmlElement(name = "PrintedDate")
    protected String printedDate;
    @XmlElement(name = "Interview")
    protected List<DailyInterviewRoster.Interview> interview;

    
     /**
     * Gets the value of the hiringEventName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHiringEventName() {
        return hiringEventName;
    }

    /**
     * Sets the value of the hiringEventName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHiringEventName(String value) {
        this.hiringEventName = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocation(String value) {
        this.location = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the stateCd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateCd() {
        return stateCd;
    }

    /**
     * Sets the value of the stateCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateCd(String value) {
        this.stateCd = value;
    }

    /**
     * Gets the value of the zipCd property.
     * 
     */
    public String getZipCd() {
        return zipCd;
    }

    /**
     * Sets the value of the zipCd property.
     * 
     */
    public void setZipCd(String value) {
        this.zipCd = value;
    }

    
    public String getPrintedDate() {
		return printedDate;
	}

	public void setPrintedDate(String printedDate) {
		this.printedDate = printedDate;
	}
    /**
     * Gets the value of the interview property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the interview property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInterview().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DailyInterviewRoster.Interview }
     * 
     * 
     */
    public List<DailyInterviewRoster.Interview> getInterview() {
        if (interview == null) {
            interview = new ArrayList<DailyInterviewRoster.Interview>();
        }
        return this.interview;
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
     *         &lt;element name="candidate" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="HiringStore" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="HiringStoreDeptNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="HiringStoreJobCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="RSCPhoneScreenNo" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                   &lt;element name="CandidateName" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="SSN" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="CandidatePhNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="CandidateEmail" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="RequisitionNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="StoreNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="DepartmentNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="JobCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="Date" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="Time" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="TimeSlot" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "candidate"
    })
    public static class Interview {

        protected List<DailyInterviewRoster.Interview.Candidate> candidate;
        @XmlAttribute(name = "TimeSlot")
        protected String timeSlot;

        /**
         * Gets the value of the candidate property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the candidate property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCandidate().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DailyInterviewRoster.Interview.Candidate }
         * 
         * 
         */
        public List<DailyInterviewRoster.Interview.Candidate> getCandidate() {
            if (candidate == null) {
                candidate = new ArrayList<DailyInterviewRoster.Interview.Candidate>();
            }
            return this.candidate;
        }

        /**
         * Gets the value of the timeSlot property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTimeSlot() {
            return timeSlot;
        }

        /**
         * Sets the value of the timeSlot property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTimeSlot(String value) {
            this.timeSlot = value;
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
         *         &lt;element name="HiringStore" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="HiringStoreDeptNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="HiringStoreJobCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="RSCPhoneScreenNo" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *         &lt;element name="CandidateName" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="SSN" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="CandidatePhNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="CandidateEmail" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="RequisitionNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="StoreNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="DepartmentNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="JobCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="Date" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="Time" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
            "hiringStore",
            "hiringStoreDeptNo",
            "hiringStoreJobCode",
            "rscPhoneScreenNo",
            "candidateName",
            "candRefNbr",
            "candidatePhNo",
            "candidateEmail",
            "requisitionNumber",
            "storeNo",
            "departmentNo",
            "jobCode",
            "date",
            "time"
        })
        public static class Candidate {

            @XmlElement(name = "HiringStore", required = true)
            protected String hiringStore;
            @XmlElement(name = "HiringStoreDeptNo", required = true)
            protected String hiringStoreDeptNo;
            @XmlElement(name = "HiringStoreJobCode", required = true)
            protected String hiringStoreJobCode;
            @XmlElement(name = "RSCPhoneScreenNo", required = true)
            protected String rscPhoneScreenNo;
            @XmlElement(name = "CandidateName", required = true)
            protected String candidateName;
            @XmlElement(name = "CandRefNbr", required = true)
            protected String candRefNbr;            
            @XmlElement(name = "CandidatePhNo", required = true)
            protected String candidatePhNo;
            @XmlElement(name = "CandidateEmail", required = true)
            protected String candidateEmail;
            @XmlElement(name = "RequisitionNumber", required = true)
            protected String requisitionNumber;
            @XmlElement(name = "StoreNo", required = true)
            protected String storeNo;
            @XmlElement(name = "DepartmentNo", required = true)
            protected String departmentNo;
            @XmlElement(name = "JobCode", required = true)
            protected String jobCode;
            @XmlElement(name = "Date", required = true)
            protected String date;
            @XmlElement(name = "Time", required = true)
            protected String time;
            

            /**
             * Gets the value of the hiringStore property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getHiringStore() {
                return hiringStore;
            }

            /**
             * Sets the value of the hiringStore property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setHiringStore(String value) {
                this.hiringStore = value;
            }

            /**
             * Gets the value of the hiringStoreDeptNo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getHiringStoreDeptNo() {
                return hiringStoreDeptNo;
            }

            /**
             * Sets the value of the hiringStoreDeptNo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setHiringStoreDeptNo(String value) {
                this.hiringStoreDeptNo = value;
            }

            /**
             * Gets the value of the hiringStoreJobCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getHiringStoreJobCode() {
                return hiringStoreJobCode;
            }

            /**
             * Sets the value of the hiringStoreJobCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setHiringStoreJobCode(String value) {
                this.hiringStoreJobCode = value;
            }

            /**
             * Gets the value of the rscPhoneScreenNo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getRSCPhoneScreenNo() {
                return rscPhoneScreenNo;
            }

            /**
             * Sets the value of the rscPhoneScreenNo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setRSCPhoneScreenNo(String value) {
                this.rscPhoneScreenNo = value;
            }

            /**
             * Gets the value of the candidateName property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCandidateName() {
                return candidateName;
            }

            /**
             * Sets the value of the candidateName property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCandidateName(String value) {
                this.candidateName = value;
            }

            public String getCandRefNbr() {
                return candRefNbr;
            }
            public void setCandRefNbr(String value) {
                this.candRefNbr = value;
            }            
            
                        
            /**
             * Gets the value of the candidatePhNo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCandidatePhNo() {
                return candidatePhNo;
            }

            /**
             * Sets the value of the candidatePhNo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCandidatePhNo(String value) {
                this.candidatePhNo = value;
            }

            /**
             * Gets the value of the candidateEmail property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCandidateEmail() {
                return candidateEmail;
            }

            /**
             * Sets the value of the candidateEmail property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCandidateEmail(String value) {
                this.candidateEmail = value;
            }

            /**
             * Gets the value of the requisitionNumber property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getRequisitionNumber() {
                return requisitionNumber;
            }

            /**
             * Sets the value of the requisitionNumber property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setRequisitionNumber(String value) {
                this.requisitionNumber = value;
            }

            /**
             * Gets the value of the storeNo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getStoreNo() {
                return storeNo;
            }

            /**
             * Sets the value of the storeNo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setStoreNo(String value) {
                this.storeNo = value;
            }

            /**
             * Gets the value of the departmentNo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDepartmentNo() {
                return departmentNo;
            }

            /**
             * Sets the value of the departmentNo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDepartmentNo(String value) {
                this.departmentNo = value;
            }

            /**
             * Gets the value of the jobCode property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getJobCode() {
                return jobCode;
            }

            /**
             * Sets the value of the jobCode property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setJobCode(String value) {
                this.jobCode = value;
            }

            /**
             * Gets the value of the date property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDate() {
                return date;
            }

            /**
             * Sets the value of the date property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDate(String value) {
                this.date = value;
            }

            /**
             * Gets the value of the time property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getTime() {
                return time;
            }

            /**
             * Sets the value of the time property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setTime(String value) {
                this.time = value;
            }

        }

    }

}
