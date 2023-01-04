/**
 * IPv4 Subnetting Calculator
 * Autor: Gisela Wolf
 * Datum: 31.12.2022
 * CW: weird and denglish variable names. Read at your own risk
 *
 * Issues still to fix:
 * - Userproofing inputs
 * - Same size nets in different size network option double up
 * - cleanup and consolidate loops
 * - add option for entering slash notation
 * - translate all into english
 */

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.lang.Integer;
import java.lang.String;
import static java.lang.Math.*;

class IPv4 {
    //IP Adress
    static int[] IPv4Dec = {0, 0, 0, 0};
    static int[] IPv4BinInt = {0, 0, 0, 0};
    static String[] IPv4BinString = {"0", "0", "0", "0"};

    //Subnetmask
    static int[] subnetDec = {0, 0, 0, 0};
    static int[] subnetBinInt = {0, 0, 0, 0};
    static String[] subnetBinString = {"0", "0", "0", "0"};

    static int subnetSlash = 0;
    static double newSubnetSlash = 0;
    static int numberOfNetworks;
    static boolean isSameSize = true;

    //Constructor
    IPv4() {
    }

    // query for IP adress and Subnetmask
    public static void encoding() {
        Scanner in = new Scanner(System.in);
//input IP
        System.out.println("First the IP-Adress:");
        System.out.println("Please enter the first octet:");
        IPv4Dec[0] = in.nextInt();
        System.out.println("Please enter the second octet:");
        IPv4Dec[1] = in.nextInt();
        System.out.println("Please enter the third octet:");
        IPv4Dec[2] = in.nextInt();
        System.out.println("Please enter the fourth octet:");
        IPv4Dec[3] = in.nextInt();
//converts to binary
        for (int i = 0; i < IPv4Dec.length; i++) {
            IPv4BinInt[i] = Integer.parseInt(Integer.toBinaryString(IPv4Dec[i]));
            IPv4BinString[i] = Integer.toBinaryString(IPv4Dec[i]);
            if (IPv4BinString[i].length() < 8) {
                IPv4BinString[i] = String.format("%08d", IPv4BinInt[i]);
            }
        }
//input Subnetmask
        System.out.println("And now the subnetmask please. Do you wish to enter it in octets (O) or slash-notation (S)? ");
        Scanner in2 = new Scanner(System.in);
        String octetOrSlash = in2.nextLine();

        if(octetOrSlash.equals("O")) {
            System.out.println("Please enter the first octet:");
            subnetDec[0] = in.nextInt();
            System.out.println("Please enter the second octet:");
            subnetDec[1] = in.nextInt();
            System.out.println("Please enter the third octet:");
            subnetDec[2] = in.nextInt();
            System.out.println("Please enter the fourth octet:");
            subnetDec[3] = in.nextInt();
        }else if(octetOrSlash.equals("S")){
            System.out.println("Please enter the \\subnetmask");
            subnetSlash = in.nextInt();
        }

//converts to binary and then to /-notation
        for (int i = 0; i < subnetDec.length; i++) {
            subnetBinString[i] = Integer.toBinaryString(subnetDec[i]);
            subnetBinInt[i] = Integer.parseInt(Integer.toBinaryString(subnetDec[i]));
            subnetSlash = subnetSlash + Integer.bitCount(subnetDec[i]);

            if (subnetBinString[i].length() < 8) {
                subnetBinString[i] = String.format("%08d", subnetBinInt[i]);      //padding
            }
        }
    }

    // query for number and size of subnets
    public static void NetzanzahlUndGroessen() {                           //collects desired subnets and returns new subnetmask, ID and BA and number of hosts
        String eingabe;

        Scanner in3 = new Scanner(System.in);
        System.out.println("Willkommen in Tikas IP Rechner");
        System.out.println("Wie viele Netze willst du bilden?");
        numberOfNetworks = in3.nextInt();
        System.out.println("Sollen die Netze gleich groß sein? Y/N");
        Scanner in2 = new Scanner(System.in);
        eingabe = in2.nextLine();

//sind die Netze gleich groß oder nicht?
        if (eingabe.equals("N")) {
            isSameSize = false;
        } else if (eingabe.equals("Y")) {
            isSameSize = true;
        }
    }

    //collects IP and subnetmask and converts them to binary and slash-notation

//Networks of the same size
    public static void gleichGrosseNetze() {
        String IPv4BinStringAll = "";

        int[] IPv4BinBitwise = new int[32];

//turn into in bitwise array
        //turn into String
        IPv4BinStringAll = IPv4BinStringAll.concat(IPv4BinString[0]).concat(IPv4BinString[1]).concat(IPv4BinString[2]).concat(IPv4BinString[3]);
        //turn into char array
        char[] IPv4BinStringAllArray = IPv4BinStringAll.toCharArray();
        //turn into int array
        for (int i = 0; i < 32; i++) {
            IPv4BinBitwise[i] = Character.getNumericValue(IPv4BinStringAllArray[i]);
        }

        //determine number of bits to flip. Beware the floating point nonsense!
        double numberOfBitsToFlip = log(numberOfNetworks) / log(2);
        int numberOfBitsToFlipRoundedUp = (int) numberOfBitsToFlip + 1;           //the number of subnets we make no we don't just make one more... we go to the next increment
        int numberOfBitsToFlipRoundedDown = (int) numberOfBitsToFlip;            //the number of subnets we give out

        int[] counterDez = new int[numberOfNetworks];
        String[] counterBinString = new String[numberOfNetworks];
        int[] counterBinInt = new int[numberOfNetworks];
        int[][] alternatesArray = new int[numberOfNetworks][numberOfBitsToFlipRoundedDown];
        int[][] alternatesArray2 = new int[numberOfNetworks][numberOfBitsToFlipRoundedUp];

        //This is the good one. Number of bits is even and we proceed to make all the subnets we output
        //this is where the magic of subnetting happens
        int[][] iPv4BinOctetwise = new int[0][];
    if (numberOfBitsToFlip == numberOfBitsToFlipRoundedDown) {
            //make array out of the bits we need to increment
            int[] flippingBitsArray = new int[numberOfBitsToFlipRoundedDown];
            for (int i = 1; i < numberOfBitsToFlip; i++) {
                flippingBitsArray[i] = IPv4BinBitwise[subnetSlash + i];
            }
            //counter of increments +1 in Decimal
            for (int i = 0; i < numberOfNetworks; i++) {
                counterDez[i] = i;
            }
            //counter of increments in binary (+padding)
            for (int i = 0; i < numberOfNetworks; i++) {
                counterBinString[i] = Integer.toBinaryString(counterDez[i]);
                counterBinInt[i] = Integer.parseInt(counterBinString[i]);
                counterBinString[i] = String.format("%0" + numberOfBitsToFlipRoundedDown + "d", counterBinInt[i]);
            }
            //alternatesArray[i][j]  i counts down, j counts across
            for (int i = 0; i < numberOfNetworks; i++) {
                for (int j = 0; j < numberOfBitsToFlipRoundedDown; j++) {
                    alternatesArray[i][j] = counterBinString[i].charAt(j) - 48;          //wtf!? but it works
                }
            }

            //feed those one by one back into the IP address to receive the variants (before ID and BA calc)
            for (int i = 0; i < numberOfNetworks; i++) {                      //for every network
                int iPv4DezOctet0 = 0;
                int iPv4DezOctet1 = 0;
                int iPv4DezOctet2 = 0;
                int iPv4DezOctet3 = 0;
                int iPv4DezOctet0BA = 0;
                int iPv4DezOctet1BA = 0;
                int iPv4DezOctet2BA = 0;
                int iPv4DezOctet3BA = 0;
                for (int j = 0; j < numberOfBitsToFlip; j++) {               //for every Bit
                    IPv4BinBitwise[subnetSlash + j] = alternatesArray[i][j];

                //ID
                    //All host bits to zero
                    for (int x = subnetSlash + (int)numberOfBitsToFlip; x < 32; x++) {
                        IPv4BinBitwise[x] = 0;
                    }
                    //Seperate into Octets
                    int[] iPv4BinOctet0 = Arrays.copyOfRange(IPv4BinBitwise, 0, 8);
                    int[] iPv4BinOctet1 = Arrays.copyOfRange(IPv4BinBitwise, 8, 16);
                    int[] iPv4BinOctet2 = Arrays.copyOfRange(IPv4BinBitwise, 16, 24);
                    int[] iPv4BinOctet3 = Arrays.copyOfRange(IPv4BinBitwise, 24, 32);
                    //convert to decimal
                    String iPv4BinStringOctet0 = "" + iPv4BinOctet0[0] + iPv4BinOctet0[1] + iPv4BinOctet0[2] + iPv4BinOctet0[3] + iPv4BinOctet0[4] + iPv4BinOctet0[5] + iPv4BinOctet0[6] + iPv4BinOctet0[7];
                    String iPv4BinStringOctet1 = "" + iPv4BinOctet1[0] + iPv4BinOctet1[1] + iPv4BinOctet1[2] + iPv4BinOctet1[3] + iPv4BinOctet1[4] + iPv4BinOctet1[5] + iPv4BinOctet1[6] + iPv4BinOctet1[7];
                    String iPv4BinStringOctet2 = "" + iPv4BinOctet2[0] + iPv4BinOctet2[1] + iPv4BinOctet2[2] + iPv4BinOctet2[3] + iPv4BinOctet2[4] + iPv4BinOctet2[5] + iPv4BinOctet2[6] + iPv4BinOctet2[7];
                    String iPv4BinStringOctet3 = "" + iPv4BinOctet3[0] + iPv4BinOctet3[1] + iPv4BinOctet3[2] + iPv4BinOctet3[3] + iPv4BinOctet3[4] + iPv4BinOctet3[5] + iPv4BinOctet3[6] + iPv4BinOctet3[7];
                    iPv4DezOctet0 = Integer.parseInt(iPv4BinStringOctet0, 2);
                    iPv4DezOctet1 = Integer.parseInt(iPv4BinStringOctet1, 2);
                    iPv4DezOctet2 = Integer.parseInt(iPv4BinStringOctet2, 2);
                    iPv4DezOctet3 = Integer.parseInt(iPv4BinStringOctet3, 2);

                //BA
                    //All host bits to one
                   // int[] IPv4BinBitwiseBA = IPv4BinBitwise;
                    for (int x = subnetSlash +((int)numberOfBitsToFlip); x < 32; x++) {
                        IPv4BinBitwise[x] = 1;
                    }
                    int[] iPv4BinOctet0BA = Arrays.copyOfRange(IPv4BinBitwise, 0, 8);
                    int[] iPv4BinOctet1BA = Arrays.copyOfRange(IPv4BinBitwise, 8, 16);
                    int[] iPv4BinOctet2BA = Arrays.copyOfRange(IPv4BinBitwise, 16, 24);
                    int[] iPv4BinOctet3BA = Arrays.copyOfRange(IPv4BinBitwise, 24, 32);
                    //convert to decimal
                    String iPv4BinStringOctet0BA = "" + iPv4BinOctet0BA[0] + iPv4BinOctet0BA[1] + iPv4BinOctet0BA[2] + iPv4BinOctet0BA[3] + iPv4BinOctet0BA[4] + iPv4BinOctet0BA[5] + iPv4BinOctet0BA[6] + iPv4BinOctet0BA[7];
                    String iPv4BinStringOctet1BA = "" + iPv4BinOctet1BA[0] + iPv4BinOctet1BA[1] + iPv4BinOctet1BA[2] + iPv4BinOctet1BA[3] + iPv4BinOctet1BA[4] + iPv4BinOctet1BA[5] + iPv4BinOctet1BA[6] + iPv4BinOctet1BA[7];
                    String iPv4BinStringOctet2BA = "" + iPv4BinOctet2BA[0] + iPv4BinOctet2BA[1] + iPv4BinOctet2BA[2] + iPv4BinOctet2BA[3] + iPv4BinOctet2BA[4] + iPv4BinOctet2BA[5] + iPv4BinOctet2BA[6] + iPv4BinOctet2BA[7];
                    String iPv4BinStringOctet3BA = "" + iPv4BinOctet3BA[0] + iPv4BinOctet3BA[1] + iPv4BinOctet3BA[2] + iPv4BinOctet3BA[3] + iPv4BinOctet3BA[4] + iPv4BinOctet3BA[5] + iPv4BinOctet3BA[6] + iPv4BinOctet3BA[7];
                    iPv4DezOctet0BA = Integer.parseInt(iPv4BinStringOctet0BA, 2);
                    iPv4DezOctet1BA = Integer.parseInt(iPv4BinStringOctet1BA, 2);
                    iPv4DezOctet2BA = Integer.parseInt(iPv4BinStringOctet2BA, 2);
                    iPv4DezOctet3BA = Integer.parseInt(iPv4BinStringOctet3BA, 2);
                }

                System.out.println("**===================**"
                        + "\r\n" + "ID: " + iPv4DezOctet0 + "." + iPv4DezOctet1 + "." + iPv4DezOctet2 + "." + iPv4DezOctet3
                        + "\r\n" + "BA: " + iPv4DezOctet0BA + "." + iPv4DezOctet1BA + "." + iPv4DezOctet2BA + "." + iPv4DezOctet3BA
                        + "\r\n" + "**===================**");

            }
        }
//else nonsense goes here. We make more nets than we output! So round up and round down
       else {
            int[] flippingBitsArray = new int[numberOfBitsToFlipRoundedDown + 1];
            for (int i = 1; i < numberOfBitsToFlip; i++) {
                flippingBitsArray[i] = IPv4BinBitwise[subnetSlash + i];
            }
            for (int i = 0; i < numberOfNetworks; i++) {
                counterDez[i] = i;
            }
            for (int i = 0; i < numberOfNetworks; i++) {
                counterBinString[i] = Integer.toBinaryString(counterDez[i]);
                counterBinInt[i] = Integer.parseInt(counterBinString[i]);
                counterBinString[i] = String.format("%0" + numberOfBitsToFlipRoundedUp +"d", counterBinInt[i]);
            }
            for (int i = 0; i < numberOfNetworks; i++) {
                for (int j = 0; j < numberOfBitsToFlipRoundedDown + 1; j++) {
                    alternatesArray2[i][j] = counterBinString[i].charAt(j) -48;          //wtf!? but it works
                }
            }

            for (int i = 0; i < numberOfNetworks; i++) {                                     //for every network
                for (int j = 0; j < numberOfBitsToFlipRoundedDown + 1; j++) {               //for every Bit
                    IPv4BinBitwise[subnetSlash - 1 + j] = alternatesArray2[i][j];
                }

                //ID
                //All host bits to zero
                for (int x = subnetSlash + numberOfBitsToFlipRoundedUp; x < 32; x++){
                    IPv4BinBitwise[x] = 0;
                         }

                //Seperate into Octets
                int[] iPv4BinOctet0 = Arrays.copyOfRange(IPv4BinBitwise, 0, 8);
                int[] iPv4BinOctet1 = Arrays.copyOfRange(IPv4BinBitwise, 8, 16);
                int[] iPv4BinOctet2 = Arrays.copyOfRange(IPv4BinBitwise, 16, 24);
                int[] iPv4BinOctet3 = Arrays.copyOfRange(IPv4BinBitwise, 24, 32);;

                //convert to decimal
                String iPv4BinStringOctet0 = "" +iPv4BinOctet0[0] +iPv4BinOctet0[1] +iPv4BinOctet0[2] +iPv4BinOctet0[3] +iPv4BinOctet0[4] +iPv4BinOctet0[5] +iPv4BinOctet0[6] +iPv4BinOctet0[7];
                String iPv4BinStringOctet1 = "" +iPv4BinOctet1[0] +iPv4BinOctet1[1] +iPv4BinOctet1[2] +iPv4BinOctet1[3] +iPv4BinOctet1[4] +iPv4BinOctet1[5] +iPv4BinOctet1[6] +iPv4BinOctet1[7];
                String iPv4BinStringOctet2 = "" +iPv4BinOctet2[0] +iPv4BinOctet2[1] +iPv4BinOctet2[2] +iPv4BinOctet2[3] +iPv4BinOctet2[4] +iPv4BinOctet2[5] +iPv4BinOctet2[6] +iPv4BinOctet2[7];
                String iPv4BinStringOctet3 ="" +iPv4BinOctet3[0] +iPv4BinOctet3[1] +iPv4BinOctet3[2] +iPv4BinOctet3[3] +iPv4BinOctet3[4] +iPv4BinOctet3[5] +iPv4BinOctet3[6] +iPv4BinOctet3[7];

                int iPv4DezOctet0 = Integer.parseInt(iPv4BinStringOctet0, 2);
                int iPv4DezOctet1 = Integer.parseInt(iPv4BinStringOctet1, 2);
                int iPv4DezOctet2 = Integer.parseInt(iPv4BinStringOctet2, 2);
                int iPv4DezOctet3 = Integer.parseInt(iPv4BinStringOctet3, 2);


                //BA
                //All host bits to one
                int[]IPv4BinBitwiseBA = IPv4BinBitwise;
                for (int x = subnetSlash + numberOfBitsToFlipRoundedUp; x < 32; x++) {
                   IPv4BinBitwiseBA[x] = 1;
                }
                int[] iPv4BinOctet0BA = Arrays.copyOfRange(IPv4BinBitwiseBA, 0, 8);
                int[] iPv4BinOctet1BA = Arrays.copyOfRange(IPv4BinBitwiseBA, 8, 16);
                int[] iPv4BinOctet2BA = Arrays.copyOfRange(IPv4BinBitwiseBA, 16, 24);
                int[] iPv4BinOctet3BA = Arrays.copyOfRange(IPv4BinBitwiseBA, 24, 32);;

                //convert to decimal
                String iPv4BinStringOctet0BA = "" +iPv4BinOctet0BA[0] +iPv4BinOctet0BA[1] +iPv4BinOctet0BA[2] +iPv4BinOctet0BA[3] +iPv4BinOctet0BA[4] +iPv4BinOctet0BA[5] +iPv4BinOctet0BA[6] +iPv4BinOctet0BA[7];
                String iPv4BinStringOctet1BA = "" +iPv4BinOctet1BA[0] +iPv4BinOctet1BA[1] +iPv4BinOctet1BA[2] +iPv4BinOctet1BA[3] +iPv4BinOctet1BA[4] +iPv4BinOctet1BA[5] +iPv4BinOctet1BA[6] +iPv4BinOctet1BA[7];
                String iPv4BinStringOctet2BA = "" +iPv4BinOctet2BA[0] +iPv4BinOctet2BA[1] +iPv4BinOctet2BA[2] +iPv4BinOctet2BA[3] +iPv4BinOctet2BA[4] +iPv4BinOctet2BA[5] +iPv4BinOctet2BA[6] +iPv4BinOctet2BA[7];
                String iPv4BinStringOctet3BA ="" +iPv4BinOctet3BA[0] +iPv4BinOctet3BA[1] +iPv4BinOctet3BA[2] +iPv4BinOctet3BA[3] +iPv4BinOctet3BA[4] +iPv4BinOctet3BA[5] +iPv4BinOctet3BA[6] +iPv4BinOctet3BA[7];

                int iPv4DezOctet0BA = Integer.parseInt(iPv4BinStringOctet0BA, 2);
                int iPv4DezOctet1BA = Integer.parseInt(iPv4BinStringOctet1BA, 2);
                int iPv4DezOctet2BA = Integer.parseInt(iPv4BinStringOctet2BA, 2);
                int iPv4DezOctet3BA = Integer.parseInt(iPv4BinStringOctet3BA, 2);

                System.out.println("**===================**"
                        + "\r\n" +"ID: " + iPv4DezOctet0 +"." +iPv4DezOctet1 +"." +iPv4DezOctet2 +"." +iPv4DezOctet3
                        + "\r\n" +"BA: " + iPv4DezOctet0BA +"." +iPv4DezOctet1BA +"." +iPv4DezOctet2BA +"." +iPv4DezOctet3BA
                        + "\r\n" +"**===================**");

            }
            }
        }

    //Networks of different sizes
    public static void unterschiedlichGrosseNetze() {
        Scanner in = new Scanner(System.in);
        Integer[] anzahlHosts = new Integer[numberOfNetworks];
        int[] nearestPowerOfTwo = new int[numberOfNetworks];
        int anzahlHostsGesamt = 0;
        int anzahlHostsGesamtMoeglich = 0;
        String IPv4BinStringAll = "";
        int[] IPv4BinBitwise = new int[32];
        int[] IPv4BinBitwiseBA = new int[32];

        //get the number of hosts and turn into anzahlHosts[]
        for (int i = 0; i < numberOfNetworks; i++) {
            System.out.println("Gib die Anzahl der Hosts für Netz " + i + " ein:");
            anzahlHosts[i] = in.nextInt() +2;       //+2 für ID und BA
            nearestPowerOfTwo[i] = (int)pow(2, ceil(log(anzahlHosts[i])/log(2)));
            anzahlHosts[i] = nearestPowerOfTwo[i];
        }

        //sort the number of hosts largest to smallest
            List<Integer> anzahlHostsList = Arrays.asList(anzahlHosts);
            Collections.sort(anzahlHostsList);
            Collections.reverse(anzahlHostsList);
            for(int i = 0; i < anzahlHostsList.size(); i++){
                anzahlHosts[i]  = anzahlHostsList.get(i);
            }
            //Ich dreh am Rad...

        //gesamtanzahl der eingegebenen Hosts, vergleich mit möglicher Hostanzahl
        for (int i = 0; i < numberOfNetworks; i++){
            anzahlHostsGesamt = anzahlHostsGesamt + anzahlHosts[i];
        }
        anzahlHostsGesamtMoeglich = (int)Math.pow(2, 32-subnetSlash);
        if(anzahlHostsGesamt <= anzahlHostsGesamtMoeglich) {

            //turn IP into String
            IPv4BinStringAll = IPv4BinStringAll.concat(IPv4BinString[0]).concat(IPv4BinString[1]).concat(IPv4BinString[2]).concat(IPv4BinString[3]);
            //turn IP into char array
            char[] IPv4BinStringAllArray = IPv4BinStringAll.toCharArray();
            //turn IP into int array
            for (int i = 0; i < 32; i++) {
                IPv4BinBitwise[i] = Character.getNumericValue(IPv4BinStringAllArray[i]);
            }
            //set all Host bits to zero
            for (int i = subnetSlash; i < 32; i++){
                IPv4BinBitwise[i] = 0;
            }

            //here starts the big for loop, one for each new subnet
            for (int i = 0; i < numberOfNetworks; i++) {
                //subnetzSlash      <-starting subnetmask
                //Determine new subnetzmask
                newSubnetSlash = subnetSlash + (log(anzahlHostsGesamtMoeglich/anzahlHosts[i])/log(2));

                //apply newSubnetSlash to IPv4BinBitwise and save the result
                IPv4BinBitwise[(int)newSubnetSlash -2] = 1;

                //BA (all Host bits to 1)
                for (int j = 0; j < 32; j++) {
                    if (j < newSubnetSlash) {
                    IPv4BinBitwiseBA[j] = IPv4BinBitwise[j];
                }else{
                        IPv4BinBitwiseBA[j] = 1;
                    }
                }

               //ID
                //Seperate into Octets
                int[] iPv4BinOctet0 = Arrays.copyOfRange(IPv4BinBitwise, 0, 8);
                int[] iPv4BinOctet1 = Arrays.copyOfRange(IPv4BinBitwise, 8, 16);
                int[] iPv4BinOctet2 = Arrays.copyOfRange(IPv4BinBitwise, 16, 24);
                int[] iPv4BinOctet3 = Arrays.copyOfRange(IPv4BinBitwise, 24, 32);;
                //convert to Strings
                String iPv4BinStringOctet0 = "" +iPv4BinOctet0[0] +iPv4BinOctet0[1] +iPv4BinOctet0[2] +iPv4BinOctet0[3] +iPv4BinOctet0[4] +iPv4BinOctet0[5] +iPv4BinOctet0[6] +iPv4BinOctet0[7];
                String iPv4BinStringOctet1 = "" +iPv4BinOctet1[0] +iPv4BinOctet1[1] +iPv4BinOctet1[2] +iPv4BinOctet1[3] +iPv4BinOctet1[4] +iPv4BinOctet1[5] +iPv4BinOctet1[6] +iPv4BinOctet1[7];
                String iPv4BinStringOctet2 = "" +iPv4BinOctet2[0] +iPv4BinOctet2[1] +iPv4BinOctet2[2] +iPv4BinOctet2[3] +iPv4BinOctet2[4] +iPv4BinOctet2[5] +iPv4BinOctet2[6] +iPv4BinOctet2[7];
                String iPv4BinStringOctet3 ="" +iPv4BinOctet3[0] +iPv4BinOctet3[1] +iPv4BinOctet3[2] +iPv4BinOctet3[3] +iPv4BinOctet3[4] +iPv4BinOctet3[5] +iPv4BinOctet3[6] +iPv4BinOctet3[7];
                //convert to decimal
                int iPv4DezOctet0 = Integer.parseInt(iPv4BinStringOctet0, 2);
                int iPv4DezOctet1 = Integer.parseInt(iPv4BinStringOctet1, 2);
                int iPv4DezOctet2 = Integer.parseInt(iPv4BinStringOctet2, 2);
                int iPv4DezOctet3 = Integer.parseInt(iPv4BinStringOctet3, 2);


                //BA
                //Seperate into Octets
                int[] iPv4BinOctet0BA = Arrays.copyOfRange(IPv4BinBitwiseBA, 0, 8);
                int[] iPv4BinOctet1BA = Arrays.copyOfRange(IPv4BinBitwiseBA, 8, 16);
                int[] iPv4BinOctet2BA = Arrays.copyOfRange(IPv4BinBitwiseBA, 16, 24);
                int[] iPv4BinOctet3BA = Arrays.copyOfRange(IPv4BinBitwiseBA, 24, 32);;
                //convert to Strings
                String iPv4BinStringOctet0BA = "" +iPv4BinOctet0BA[0] +iPv4BinOctet0BA[1] +iPv4BinOctet0BA[2] +iPv4BinOctet0BA[3] +iPv4BinOctet0BA[4] +iPv4BinOctet0BA[5] +iPv4BinOctet0BA[6] +iPv4BinOctet0BA[7];
                String iPv4BinStringOctet1BA = "" +iPv4BinOctet1BA[0] +iPv4BinOctet1BA[1] +iPv4BinOctet1BA[2] +iPv4BinOctet1BA[3] +iPv4BinOctet1BA[4] +iPv4BinOctet1BA[5] +iPv4BinOctet1BA[6] +iPv4BinOctet1BA[7];
                String iPv4BinStringOctet2BA = "" +iPv4BinOctet2BA[0] +iPv4BinOctet2BA[1] +iPv4BinOctet2BA[2] +iPv4BinOctet2BA[3] +iPv4BinOctet2BA[4] +iPv4BinOctet2BA[5] +iPv4BinOctet2BA[6] +iPv4BinOctet2BA[7];
                String iPv4BinStringOctet3BA ="" +iPv4BinOctet3BA[0] +iPv4BinOctet3BA[1] +iPv4BinOctet3BA[2] +iPv4BinOctet3BA[3] +iPv4BinOctet3BA[4] +iPv4BinOctet3BA[5] +iPv4BinOctet3BA[6] +iPv4BinOctet3BA[7];
                //convert to decimal
                int iPv4DezOctet0BA = Integer.parseInt(iPv4BinStringOctet0BA, 2);
                int iPv4DezOctet1BA = Integer.parseInt(iPv4BinStringOctet1BA, 2);
                int iPv4DezOctet2BA = Integer.parseInt(iPv4BinStringOctet2BA, 2);
                int iPv4DezOctet3BA = Integer.parseInt(iPv4BinStringOctet3BA, 2);

                System.out.println("**==============================**"
                        +"\r\n" +(i+1)  +". new Subnet:"
                        +"\r\n" +"IP:                "  + iPv4DezOctet0 +"." +iPv4DezOctet1 +"." +iPv4DezOctet2 +"." +iPv4DezOctet3
                        +"\r\n" +"BA:                " + iPv4DezOctet0BA +"." +iPv4DezOctet1BA +"." +iPv4DezOctet2BA +"." +iPv4DezOctet3BA
                        +"\r\n" +"New Subnetmask:    /" +newSubnetSlash
                        +"\r\n" +"Hosts:             " +(anzahlHosts[i] -2)
                        +"\r\n" +"**==============================**"
                );

            }

            //Fehler für zuviele Hosts
        }else{
            System.out.println("Sorry aber so viele Hosts passen nicht in ein \\" +subnetSlash +" Netz.");
        }

    }
}



    public class IPv4Calculator {
        public static void main(String[] args) {
            IPv4 ipv4 = new IPv4();
            //IPv4 or IPv6?
            IPv4.encoding();
            IPv4.NetzanzahlUndGroessen();

//gleichgroße Netze:
            if (IPv4.isSameSize) {
                IPv4.gleichGrosseNetze();
            }
//ungleiche Netze:
            if (!IPv4.isSameSize) {
                IPv4.unterschiedlichGrosseNetze();
            }

//option for IPv6 (same but in hex? with different padding rules. basically)

        }
    }


    /*  output
        System.out.println("Die IP:           " + IPv4BinString[0] + "." + IPv4BinString[1] + "." + IPv4BinString[2] + "." + IPv4BinString[3]);
        System.out.println("Die Subnetzmaske: " + subnetzBinString[0] + "." + subnetzBinString[1] + "." + subnetzBinString[2] + "." + subnetzBinString[3]);
        System.out.println("Slash-Notation:   \\" + subnetzSlash);
    */




