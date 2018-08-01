module namespace s = "http://www.example.org/schemas/solution";

import schema namespace c1 = "http://www.example.org/schemas/clinic" at "Clinic.xsd";

import schema namespace p1 = "http://www.example.org/schemas/clinic/patient" at "Patient.xsd";

import schema namespace tr1 = "http://www.example.org/schemas/clinic/treatment" at "Treatment.xsd";

import schema namespace p2 = "http://www.example.org/schemas/clinic/provider" at "Provider.xsd";

(:========================= Question 1 =================================:)

declare function s:getPatientTreatments ($p as xs:decimal?, $klinic as element(c1:Clinic))
as element(li, xs:untyped)*
{
for $s in $klinic/p1:patient
where $s/p1:patient-id = $p
return 
<li>
{
for $tr in $s/p1:treatments/p1:treatment
return
    <li>
    Provider Id: {data($tr/tr1:provider-id)}
    Diagnosis: {data($tr/tr1:diagnosis)}
    {
    if ($tr/tr1:drug-treatment) then
    <li>
    Drug Treatment
    Drug Name: {data($tr/tr1:drug-treatment/tr1:name)}
    Drug Dosage: {data($tr/tr1:drug-treatment/tr1:dosage)}
    </li>
    else if ($tr/tr1:radiology) then
    <li>
    Radiology
    Date: {data($tr/tr1:radiology/tr1:date)}
    </li>
    else if ($tr/tr1:surgery) then
    <li>
    Surgery
    Date: {data($tr/tr1:surgery/tr1:date)}
    </li>
    else ()
    }
    </li>
}

</li>
};

(:========================= Question 2 =================================:)


declare function s:getPatientDrugs ($p as xs:decimal?, $klinic as element(c1:Clinic))
as element(li, xs:untyped)*
{
for $s in $klinic/p1:patient
where $s/p1:patient-id = $p
return <li>
        {
            for $name1 in $s/p1:treatments/p1:treatment
               
            return if ($name1/tr1:drug-treatment) then
                    <li> Drug Name: {data($name1/tr1:drug-treatment/tr1:name)} 
                        Drug Dosage: {data($name1/tr1:drug-treatment/tr1:dosage)}
                        Diagnosis: { data($name1/tr1:diagnosis)}
                    </li>
                    else ()
        }
       </li>
};

(:========================= Question 3 =================================:)

declare function s:getTreatmentInfo ($klinic as element(c1:Clinic))
as element(li, xs:untyped)*
{
for $s in $klinic/p1:patient
return
    <li>
    {
    for $tr in $s/p1:treatments/p1:treatment
    return 
    if ($tr/tr1:drug-treatment) then
        <li>
        Drug Treatment
        Patient Id: {data($s/p1:patient-id)}
        Patient Name: {data($s/p1:name)}
        {
        for $pr in $klinic/p2:provider
        where $pr/p2:provider-id = $tr/tr1:provider-id
        return 
        <li>
        Provider Id: {data($pr/p2:provider-id)}
        Provider Name: {data($pr/p2:name)}
        </li>
        }
        </li>
    else if ($tr/tr1:radiology) then
        <li>
        Radiology        
        Patient Id: {data($s/p1:patient-id)}
        Patient Name: {data($s/p1:name)}
        {
        for $pr in $klinic/p2:provider
        where $pr/p2:provider-id = $tr/tr1:provider-id
        return 
        <li>
        Provider Id: {data($pr/p2:provider-id)}
        Provider Name: {data($pr/p2:name)}
        </li>
        }
        </li>
    else if ($tr/tr1:surgery) then
        <li>
        Surgery
        Patient Id: {data($s/p1:patient-id)}
        Patient Name: {data($s/p1:name)}
        {
        for $pr in $klinic/p2:provider
        where $pr/p2:provider-id = $tr/tr1:provider-id
        return 
        <li>
        Provider Id: {data($pr/p2:provider-id)}
        Provider Name: {data($pr/p2:name)}
        </li>
        }
        </li>
    else ()    
    }
    </li>
    

};


(:========================= Question 4 =================================:)

declare function s:getProviderInfo ($klinic as element(c1:Clinic))
as element(li, xs:untyped)*
{
for $pr in $klinic/p2:provider
   
return <li>{
        <li>
            Provider Id: {data($pr/p2:provider-id)}
            Provider Name: {data($pr/p2:name)}
       </li>
       }
       {
       for $s in $klinic/p1:patient
            where $s/p1:treatments/p1:treatment/tr1:provider-id = $pr/p2:provider-id
            return <li>
                   {
                    <li>
                        Patient ID: {data($s/p1:patient-id)}
                        Patient Name : {data($s/p1:name)}
                   </li>
                    }
                    {
                         for $trmt in $s/p1:treatments/p1:treatment
                         return
                            if ($trmt/tr1:drug-treatment and $trmt/tr1:provider-id = $pr/p2:provider-id) then
                            <li>Drug Treatment</li>
                            else if ($trmt/tr1:radiology and $trmt/tr1:provider-id = $pr/p2:provider-id) then
                            <li>RadioLogy</li>
                            else if ($trmt/tr1:surgery and $trmt/tr1:provider-id = $pr/p2:provider-id) then
                            <li>Surgery</li>
                            else ()
                    }
        </li>
        }
        
        </li>
};

(:========================= Question 5 =================================:)

declare function s:getDrugInfo ($klinic as element(c1:Clinic))
as element(li, xs:untyped)*
{
for $dt in distinct-values($klinic/p1:patient/p1:treatments/p1:treatment/tr1:drug-treatment/tr1:name)
  
return <li>
        {
            <li>
                Drug Name: {data($dt)}
                {
                for $pt in $klinic/p1:patient
                where $pt/p1:treatments/p1:treatment/tr1:drug-treatment/tr1:name = $dt
                return
                <li>
                    Patient Id: {data($pt/p1:patient-id)}
                    Patient Name: {data($pt/p1:name)}
                    {
                    for $treat in $pt/p1:treatments/p1:treatment
                    where $treat/tr1:drug-treatment/tr1:name = $dt
                    return 
                    <li>
                    Diagnosis: {data($treat/tr1:diagnosis)}
                    {
                    for $pr in $klinic/p2:provider
                    where $pr/p2:provider-id = $treat/tr1:provider-id
                    return 
                    <li>
                    Provider Id: {data($pr/p2:provider-id)}
                    Provider Name: {data($pr/p2:name)}
                    </li>
                    }
                    </li>
                    }
                    
                    
                </li>    
                
                }
            </li>
        }
        </li>
};