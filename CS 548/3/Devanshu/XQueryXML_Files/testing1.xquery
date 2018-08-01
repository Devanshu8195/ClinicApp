import module namespace s = "http://www.example.org/schemas/solution" at "Solutions.xquery";

import schema namespace c1 = "http://www.example.org/schemas/clinic" at "Clinic.xsd";


let $clinic := doc("ClinicData.xml")/c1:Clinic

return s:getPatientTreatments(748596,$clinic)






