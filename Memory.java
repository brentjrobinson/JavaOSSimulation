
public class Memory {


    private String[]disk;
    private String[] RAM;

    public Memory (int diskSize, int ramSize)
    {
        this.disk = new String[diskSize];
        this.RAM = new String[ramSize];
    }
    public String pullDisk (int location)
    {
        return disk[location];
    }

    public void writeDisk (String code, int location )
    {
        this.disk[location] = code;
    }

    public String pullRAM (int location)
    {
        return RAM[location];
    }
    public void writeRAM (String code, int location)
    {
        RAM[location] = code;
    }

    public void dumpDisk ()
    {
        for (int i = 0; i < disk.length; i++)
        {
            System.out.println(disk[i]);
        }
    }

    public void dumpRam ()
    {
        for (int i = 0; i < RAM.length; i++)
        {
            System.out.println(RAM[i]);
        }
    }

    public void percentUsed(){
        double count = 0;
        for (int i = 0; i < RAM.length; i++)
        {
            if (RAM[i] != null)
            {
                count = count+1;
            }
        }
        double perc = (count/ RAM.length) * 100;
        System.out.println("------------------------------");
        System.out.println("Percentage of RAM used: "+ Math.floor(perc * 100) / 100 + "%");
    }




}