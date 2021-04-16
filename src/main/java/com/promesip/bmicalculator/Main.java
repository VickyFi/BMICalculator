/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.promesip.bmicalculator;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Scanner;


/**
 *
 * @author vicky
 */
public class Main {
    public static void main (String[] args) throws IOException {
        
        Scanner sc = new Scanner(System.in);
        String dir = System.getProperty("user.dir"); // getting current directory path
        DecimalFormat df = new DecimalFormat("0.0"); // define decimal format to convert results
        float bmi;
        float weight;
        float height;
        int pID;
        char gender; // added gender parameter to specify perfect weight for male & female patients accordingly
        float perfectWeight;
        String FperfectWeight;
        FileWriter BMIReport = new FileWriter("bmi_results.txt", true); //create file for writing results
        
        System.out.println("This is a metric BMI calculator.\n");
        
        Scanner ch = new Scanner(System.in);
        // Display the menu
        System.out.println("1\t Input Values");
        System.out.println("2\t Read from file");

        System.out.println("Please enter your choice:");
        
        //Get user's choice
        int choice = ch.nextInt();
         
        //Display BMI results
        switch (choice) {
            case 1: // Case 1: input data from keyboard
                System.out.println("Reading patient records from input...\n\n");
                sc = new Scanner(System.in);
                
                //read and validate weight input
                System.out.println("Please enter your weight in kilograms:");
                weight = sc.nextFloat();           
                while(weight <= 25 || weight >= 240){
                    System.out.println("Your input is invalid. Please enter your weight in kilograms (accepted values only between 25-240 kg).\n");
                    weight = sc.nextFloat();
                }  
        
                //read and validate height input
                System.out.println("Please enter your height in meters:");
                height = sc.nextFloat();
                while(height <= 1.20 || height>=2.40){
                    System.out.println("Your input is invalid. Please enter your height in meters (accepted values only between 1.20-2.40 m).\n");
                    height = sc.nextFloat();
                }
                
                 //read and validate gender input
                System.out.println("Please enter your gender. Type 'F' for female or 'M' for male:");
                gender = sc.next().charAt(0);
                while(gender != 'f' && gender != 'F' && gender != 'm' && gender  != 'M'){
                    System.out.println("Your input is invalid. Proper gender values are 'F' or 'f' for female and 'M' or 'm' for male.\n");
                    gender = sc.next().charAt(0);
                }
                
                sc.close(); // close scanner
                
                //print data
                System.out.println("Patient Gender "+ gender +" patient weight: "+ weight +" patient height: "+ height +"\n");
               
                //calculate perfect weight
                if(gender == 'f' || gender == 'F'){
                    //Women: Ideal Body Weight (kg) = [Height (cm) - 100] + ([Height (cm) - 100] x 15%)  
                    perfectWeight = ((height * 100) - 100) - (((height * 100) - 100) / 15);
                    FperfectWeight = df.format(perfectWeight);
                }else{
                    //Men: Ideal Body Weight (kg) = [Height (cm) - 100] - ([Height (cm) - 100] x 10%)
                    perfectWeight = ((height * 100) - 100) - (((height * 100) - 100) / 10);
                    FperfectWeight = df.format(perfectWeight);
                }
                
                bmi = weight/(height*height); // calculate bmi
                
                if(bmi<18.5){ // print and write results for given record depenting on person's BMI value
                    System.out.println("Underweight - Perfect weight for that height is : " + FperfectWeight + " Person should gain: " + df.format((perfectWeight - weight)));
                    BMIReport.write("Person with: "+ gender +" "+ weight + " "+ height +" "+ df.format(bmi) +" - Underweight - Perfect weight for that height is : " + FperfectWeight + " Person should gain: " +  df.format((perfectWeight - weight))+"\n");
                }else if(bmi>=18.5 && bmi<=23.9){
                    System.out.println("Normal - Person has perfect weight");
                    BMIReport.write("Person with: "+ gender +" "+ weight + " "+ height +" "+ df.format(bmi) +" - Normal - Person has perfect weight\n");
                }else if(bmi>=25.0 && bmi<=29.9){
                    System.out.println("Overweight - Perfect weight for that height is : " + FperfectWeight + " Person should lose: " + df.format((weight - perfectWeight)));
                    BMIReport.write("Person with: "+ gender +" "+ weight + " "+ height +" "+ df.format(bmi) +" - Overweight - Perfect weight for that height is : " + FperfectWeight + " Person should lose: " + df.format((weight - perfectWeight))+"\n");
                }else if(bmi>=30.0){
                    System.out.println("Obese - Perfect weight for that height is : " + FperfectWeight + " Person should lose: " + df.format((weight - perfectWeight))); 
                    BMIReport.write("Person with: "+ gender +" "+ weight + " "+ height +" "+ df.format(bmi) +" - Obese - Perfect weight for that height is : " + FperfectWeight + " Person should lose: " + df.format((weight - perfectWeight))+"\n");
                }
                System.out.println("\n\n");
                break;
            case 2: //Case 2: read data from file
                System.out.println("Reading patient records from file...\n\n");
                try{ //read records from file
                    FileInputStream fstream = new FileInputStream(dir+"/patient_records.txt");
                    try (DataInputStream in = new DataInputStream(fstream)) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String strLine;
                        while ((strLine = br.readLine()) != null){
                            String[] tokens = strLine.split(" ");
                            
                            //pass each value read from file to the proper variable 
                            pID = Integer.parseInt(tokens[0]);
                            weight = Float.parseFloat(tokens[2]);
                            height = Float.parseFloat(tokens[3]);
                            gender = tokens[1].charAt(0);
                            
                            //validate gender
                            if(gender != 'f' && gender != 'F' && gender != 'm' && gender  != 'M'){
                                System.out.println("Error - Patient ID: "+ pID +" Proper gender values are 'F' or 'f' for female and 'M' or 'm' for male: "+ gender +" patient weight: "+ weight +" patient height: "+ height +"\n");
                                System.out.println("Please revise your file accordingly.\n");
                                break;
                            }
                            
                            //validate weight
                            if(weight <= 25 || weight>=240){
                                System.out.println("Error - Patient ID: "+ pID +" Gender "+ gender +" patient weight accepted values only between 25-240 kg: "+ weight +" patient height: "+ height +"\n");
                                System.out.println("Please revise your file accordingly.\n");
                                break;
                            }
                            
                            //validate height
                            if(height <= 1.20 || height>=2.40){
                                System.out.println("Error - Patient ID: "+ pID +" Gender "+ gender +" patient weight: "+ weight +" patient height accepted values only between 1.20-2.40 m: "+ height +"\n");
                                System.out.println("Please revise your file accordingly.\n");
                                break;
                            }
                            
                            //print data
                            System.out.println("Patient ID: "+ pID +" Gender "+ gender +" patient weight: "+ weight +" patient height: "+ height +"\n");
                            
                            //calculate perfect weightμέσω ενός αρχείου .txt
                            if(gender == 'f' || gender == 'F'){
                                //Women: Ideal Body Weight (kg) = [Height (cm) - 100] + ([Height (cm) - 100] x 15%)
                                perfectWeight = ((height * 100) - 100) - (((height * 100) - 100) / 15);
                                FperfectWeight = df.format(perfectWeight);
                            }else{
                                //Men: Ideal Body Weight (kg) = [Height (cm) - 100] - ([Height (cm) - 100] x 10%)
                                perfectWeight = ((height * 100) - 100) - (((height * 100) - 100) / 10);
                                FperfectWeight = df.format(perfectWeight);
                            }
                            
                            bmi = weight/(height*height); // calculate bmi
                            
                            if(bmi<18.5){ // print and write results for each record depenting on patient's BMI value
                                System.out.println("Underweight - Perfect weight for that height is : " + FperfectWeight + " Person should gain: " + df.format((perfectWeight - weight)));
                                BMIReport.write(pID +" "+ gender +" "+ weight + " "+ height +" "+ df.format(bmi) +" - Underweight - Perfect weight for that height is : " + FperfectWeight + " Person should gain: " +  df.format((perfectWeight - weight))+"\n");
                            }else if(bmi>=18.5 && bmi<=23.9){
                                System.out.println("Normal - Person has perfect weight");
                                BMIReport.write(pID +" "+ gender +" "+ weight + " "+ height +" "+ df.format(bmi) +" - Normal - Person has perfect weight\n");
                            }else if(bmi>=25.0 && bmi<=29.9){
                                System.out.println("Overweight - Perfect weight for that height is : " + FperfectWeight + " Person should lose: " + df.format((weight - perfectWeight)));
                                BMIReport.write(pID +" "+ gender +" "+ weight + " "+ height +" "+ df.format(bmi) +" - Overweight - Perfect weight for that height is : " + FperfectWeight + " Person should lose: " + df.format((weight - perfectWeight))+"\n");
                            }else if(bmi>=30.0){
                                System.out.println("Obese - Perfect weight for that height is : " + FperfectWeight + " Person should lose: " + df.format((weight - perfectWeight)));
                                BMIReport.write(pID +" "+ gender +" "+ weight + " "+ height +" "+ df.format(bmi) +" - Obese - Perfect weight for that height is : " + FperfectWeight + " Person should lose: " + df.format((weight - perfectWeight))+"\n");
                            }
                            
                            System.out.println("\n\n");
                        }
                    }
                }catch (IOException | NumberFormatException e){
                    System.err.println("Error: " + e.getMessage() + " please revise your records file accordingly.");
                } BMIReport.close(); //close reporting file
                break;
            default:
                System.out.println("Invalid choice");
                break; //end of switch
        }
       
    }    
}
