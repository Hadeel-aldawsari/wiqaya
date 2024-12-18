package com.example.wiqaya.Service;


import com.example.wiqaya.ApiResponse.ApiException;
import com.example.wiqaya.DTO.IN.ReportDTOIN;
import com.example.wiqaya.DTO.OUT.ReportDTOOUT;
import com.example.wiqaya.Model.*;
import com.example.wiqaya.Repository.EngineerRepository;
import com.example.wiqaya.Repository.ReportRepository;
import com.example.wiqaya.Repository.RequestInspectionRepository;
import com.example.wiqaya.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private  final ReportRepository reportRepository;
    private final EngineerRepository engineerRepository;
    private final RequestInspectionRepository requestInspectionRepository;
    private final UserRepository userRepository;

    public List<ReportDTOOUT> getAll(){
        List<Report>reports=reportRepository.findAll();
        if(reports.isEmpty())throw new ApiException("there is no report");

        List<ReportDTOOUT> dtos=new ArrayList<>();
        for(Report r:reports){
            ReportDTOOUT reportDTOOUT =new ReportDTOOUT(r.getId(),r.getEngineer().getId(),r.getStructuralElements(),r.getFireDetection(),r.getHeatingCookingSystems(),r.getEmergencyPreparedness(),r.getVentilationSmokeManagement()
                    ,r.getExteriorSurroundings(),r.getPercentage(),r.getNotes(),r.getRequiredItems(),r.getReportedDate());
            dtos.add(reportDTOOUT);
        }
        return  dtos;
    }


    public void CreateReport(Integer engineerId ,Integer  RequestInspectionId ,ReportDTOIN reportDTOIN){

        //check Eng
        Engineer engineer=engineerRepository.findEngineerById(engineerId);
        if(engineer==null)throw new ApiException("there is no engineer found");

        // check if the request exist && check if the engineer own this request
   RequestInspection requestInspection=requestInspectionRepository.findRequestInspectionById(RequestInspectionId);
   if(requestInspection==null)throw new ApiException("there is no RequestInspectionId by this id");

   if(!requestInspection.getEngineer().getId().equals(engineerId))throw new ApiException("the engineer didn't assign to this request inspection ");


   Boolean isPublished=false;

        int trueCount = 0;

        if (reportDTOIN.getStructuralElements()) trueCount++;
        if (reportDTOIN.getFireDetection()) trueCount++;
        if (reportDTOIN.getElectricalDange()) trueCount++;
        if (reportDTOIN.getHeatingCookingSystems()) trueCount++;
        if (reportDTOIN.getEmergencyPreparedness()) trueCount++;
        if (reportDTOIN.getHazardousMaterialsStorage()) trueCount++;
        if (reportDTOIN.getVentilationSmokeManagement()) trueCount++;
        if (reportDTOIN.getExteriorSurroundings()) trueCount++;

        // Calculate percentage (based on the 8 boolean values)
        Integer percentage = (trueCount * 100) / 8;
        House house=requestInspection.getHouse();


        // add set for condaition
        house.setConditionPercentage(percentage);
        // add set for status
       house.setStatus("checked");
        requestInspection.setStatus("Reported");
        Report report = new Report(
                null, // ID
                reportDTOIN.getStructuralElements(),
                reportDTOIN.getFireDetection(),
                reportDTOIN.getElectricalDange(),
                reportDTOIN.getHeatingCookingSystems(),
                reportDTOIN.getEmergencyPreparedness(),
                reportDTOIN.getHazardousMaterialsStorage(),
                reportDTOIN.getVentilationSmokeManagement(),
                reportDTOIN.getExteriorSurroundings(),
                percentage, // Set the percentage here
                reportDTOIN.getNotes(),
                reportDTOIN.getRequiredItems(),
                LocalDate.now(),// Set the ReportedDate as the current date
                false , //here is the first publish status by defult false
                engineer,house,
                null, // the offers are null when the report is added
                requestInspection// set reported
        );

        // Save the report to the database
        reportRepository.save(report);
    }


    // delete report
    public void delete(Integer id){
        Report report = reportRepository.findReportById(id);
        if(report==null){
            throw new ApiException("report not found");
        }
        reportRepository.delete(report);
    }


//    // Method to assign an engineer to a report
//    public Report assignEngineerToReport(Integer reportId, Integer engineerId) {
//        // Retrieve the report by ID
//        Report report = reportRepository.findById(reportId)
//                .orElseThrow(() -> new IllegalArgumentException("Report not found with ID: " + reportId));
//
//        // Retrieve the engineer by ID
//        Engineer engineer = engineerRepository.findById(engineerId)
//                .orElseThrow(() -> new IllegalArgumentException("Engineer not found with ID: " + engineerId));
//
//        // Assign the engineer to the report
//        report.setEngineer(engineer);
//
//        // Save the updated report with the assigned engineer
//        return reportRepository.save(report);
//
//    }

    public void publishReport(Integer userId, Integer reportId){
        User user = userRepository.findUserById(userId);
        if (user == null) throw new ApiException("user not found");

        Report report = reportRepository.findReportById(reportId);
        if(report==null){throw new ApiException("report not found");}

        if(!report.getRequestInspection().getHouse().getUser().getId().equals(userId)){throw new ApiException("user can't publish report, because he doesn't own the house");}

        report.setIsPublished(true);
        reportRepository.save(report);

    }
}