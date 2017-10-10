import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class Loader {

    private final String jobStart = "// JOB";
    private final String dataStart = "// Data";
    private FileReader mainInstructions;

    public Loader(FileReader instructionSet) {

        this.mainInstructions = instructionSet;

    }
    void load() throws IOException {
        System.out.println("Attempting to load file...");
        String card = "";
        String line = "";
        int number, priority, buffer, inputBuffer, outputBuffer, tempBuffer;
        int counter = 0;
        int counterInstructions = 0 ;
        BufferedReader reader = new BufferedReader(mainInstructions);
        StringTokenizer stringTokenizer = new StringTokenizer(card);
        while ((card = reader.readLine()) != null )
        {

            if (card.contains(jobStart))
            {
                line = card;
                StringTokenizer lineTokenizer = new StringTokenizer(line);
                // A job has been found, write the attributes PCB / DISK
                // pass over the //JOB
                lineTokenizer.nextToken();
                lineTokenizer.nextToken();
                number = Integer.parseInt(lineTokenizer.nextToken(),16);
                buffer= Integer.parseInt(lineTokenizer.nextToken(),16);
                priority = Integer.parseInt(lineTokenizer.nextToken(),16);
                Process job = new Process(number,priority,buffer, counter);
                Driver.PCB.addProcess(job);

                for (int i = 0; i < buffer; i++)
                {

                    String s = reader.readLine();
                    //System.out.println(s);
                    if (!s.equals(null)) {
                        Driver.memory.writeDisk(s, counter);
                        counter++;
                    }
                }


            }

            else if (card.contains(dataStart)){
                line = card;
                StringTokenizer lineTokenizer = new StringTokenizer(line);
                // A job has been found, write the attributes PCB / DISK
                // pass over the //JOB
                lineTokenizer.nextToken();
                lineTokenizer.nextToken();
                inputBuffer = Integer.parseInt(lineTokenizer.nextToken(),16);
                outputBuffer= Integer.parseInt(lineTokenizer.nextToken(),16);
                tempBuffer = Integer.parseInt(lineTokenizer.nextToken(),16);
                Driver.PCB.peekProcess().addDataInfo(inputBuffer,outputBuffer,tempBuffer);
                for (int i = 0; i < inputBuffer; i++)
                {

                    String s = reader.readLine();
                    //System.out.println(s);
                    Driver.memory.writeDisk(s,counter);
                    counter++;
                }

            }
        }
        System.out.println("File loading complete");
        reader.close();
    }



}

