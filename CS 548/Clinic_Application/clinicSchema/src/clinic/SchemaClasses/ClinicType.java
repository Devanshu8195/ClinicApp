//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.10.01 at 04:41:29 AM EDT 
//


package clinic.SchemaClasses;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ClinicType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ClinicType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;element ref="{http://www.example.org/schemas/clinic/Patient}Patient" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.example.org/schemas/clinic/Provider}Provider" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ClinicType", namespace = "http://www.example.org/schemas/clinic", propOrder = {
    "patientAndProvider"
})
public class ClinicType {

    @XmlElements({
        @XmlElement(name = "Patient", namespace = "http://www.example.org/schemas/clinic/Patient", type = PatientType.class),
        @XmlElement(name = "Provider", namespace = "http://www.example.org/schemas/clinic/Provider", type = ProviderType.class)
    })
    protected List<Object> patientAndProvider;

    /**
     * Gets the value of the patientAndProvider property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the patientAndProvider property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPatientAndProvider().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PatientType }
     * {@link ProviderType }
     * 
     * 
     */
    public List<Object> getPatientAndProvider() {
        if (patientAndProvider == null) {
            patientAndProvider = new ArrayList<Object>();
        }
        return this.patientAndProvider;
    }

}