/**
 * IPv4 Subnetting Calculator
 * author: Gisela Wolf
 * date: 19.01.2023
 * Issues still to fix:
 * - Userproofing inputs
 * - problem: Same size nets in different size network option
 * IPv6
 * - shortform input, padding rules
 * - test commit
 */

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.lang.Integer;
import java.lang.String;
import static java.lang.Math.*;

class hex{
    public static String hexToBin(String hex){
        hex = hex.replaceAll("0", "0000");
        hex = hex.replaceAll("1", "0001");
        hex = hex.replaceAll("2", "0010");
        hex = hex.replaceAll("3", "0011");
        hex = hex.replaceAll("4", "0100");
        hex = hex.replaceAll("5", "0101");
        hex = hex.replaceAll("6", "0110");
        hex = hex.replaceAll("7", "0111");
        hex = hex.replaceAll("8", "1000");
        hex = hex.replaceAll("9", "1001");
        hex = hex.replaceAll("A", "1010");
        hex = hex.replaceAll("B", "1011");
        hex = hex.replaceAll("C", "1100");
        hex = hex.replaceAll("D", "1101");
        hex = hex.replaceAll("E", "1110");
        hex = hex.replaceAll("F", "1111");
        hex = hex.replaceAll("a", "1010");
        hex = hex.replaceAll("b", "1011");
        hex = hex.replaceAll("c", "1100");
        hex = hex.replaceAll("d", "1101");
        hex = hex.replaceAll("e", "1110");
        hex = hex.replaceAll("f", "1111");
        return hex;
    }
}

class IPv4 {
    //IP Adress
    static int[] IPv4Dec = {0, 0, 0, 0};
    static int[] IPv4BinInt = {0, 0, 0, 0};
    static String[] IPv4BinString = {"0", "0", "0", "0"};

    //Subnetmask
    static int[] subnetDec = {0, 0, 0, 0};
    static int[] subnetBinInt = {0, 0, 0, 0};
    static String[] subnetBinString = {"0", "0", "0", "0"};

    private static int subnetSlash = 0;
    static double newSubnetSlash = 0;
    static int numberOfNetworksDesired;
    static Integer[] numberOfHosts = new Integer[numberOfNetworksDesired];
    static boolean isSameSize = true;

/*
    public static int[] getSubnetDec() {
        return subnetDec;
    }
    public static void setSubnetDec(int[] subnetDec) {
        if(true) {
            IPv4.subnetDec = subnetDec;
        }
    }
    public static int getSubnetSlash() {
        return subnetSlash;
    }
    public static void setSubnetSlash(int subnetSlash) {
        IPv4.subnetSlash = subnetSlash;
    }
    public static int getNumberOfNetworksDesired() {
        return numberOfNetworksDesired;
    }
    public static void setNumberOfNetworksDesired(int numberOfNetworksDesired) {
        IPv4.numberOfNetworksDesired = numberOfNetworksDesired;
    }
    public Integer[] getNumberOfHosts() {
        return numberOfHosts;
    }
    public void setNumberOfHosts(Integer[] numberOfHosts) {
        this.numberOfHosts = numberOfHosts;
    }
*/

    //Constructor
    IPv4() {
    }

    //collects IP and subnetmask and converts them to binary and slash-notation
    public static void encoding() {
        Scanner in = new Scanner(System.in);
//input IP
        System.out.println("Welcome to this IPv4 Calculator");
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

        if(octetOrSlash.equals("O") || octetOrSlash.equals("o")) {
            System.out.println("Please enter the first octet:");
            subnetDec[0] = in.nextInt();
            System.out.println("Please enter the second octet:");
            subnetDec[1] = in.nextInt();
            System.out.println("Please enter the third octet:");
            subnetDec[2] = in.nextInt();
            System.out.println("Please enter the fourth octet:");
            subnetDec[3] = in.nextInt();
        }else if(octetOrSlash.equals("S") || octetOrSlash.equals("s")){
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
    public static void numberAndSizeOfNetworks() {                           //collects desired subnets and returns new subnetmask, ID and BA and number of hosts
        String eingabe;
        Scanner in3 = new Scanner(System.in);
        System.out.println("How many networks do you want to make?");
        numberOfNetworksDesired = in3.nextInt();
        System.out.println("Should the networks be of the same size? Y/N");
        Scanner in2 = new Scanner(System.in);
        eingabe = in2.nextLine();

//are the networks of the same size?
        if (eingabe.equals("N") || eingabe.equals("n")) {
            isSameSize = false;
        } else if (eingabe.equals("Y") || eingabe.equals("y")) {
            isSameSize = true;
        }
    }

    //Networks of the same size
    public static void sameSizeNetworks() {
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

        //determine number of bits to flip. Beware the floating point!
        // numberOfNetworksDesired vs numberOfNetworks made
        double numberOfBitsToFlip = log(numberOfNetworksDesired) / log(2);
        int numberOfBitsToFlipRoundedUp = (int) numberOfBitsToFlip + 1;           //the number of subnets we make
        int numberOfBitsToFlipRoundedDown = (int) numberOfBitsToFlip;            //the number of subnets we give out

        //this is where the magic of subnetting happens
        //This is the good one. Number of bits is even and we proceed to make all the subnets we output
        int[][] iPv4BinOctetwise = new int[0][];
    if (numberOfBitsToFlip == numberOfBitsToFlipRoundedDown) {
        int numberOfNetworksMade = numberOfNetworksDesired;
        int[] counterDec = new int[numberOfNetworksMade];
        String[] counterBinString = new String[numberOfNetworksMade];
        int[] counterBinInt = new int[numberOfNetworksMade * numberOfBitsToFlipRoundedDown];
        int[][] alternatesArray = new int[numberOfNetworksMade][numberOfBitsToFlipRoundedDown];             //bits to flip even

            //make array out of the bits we need to increment
            int[] flippingBitsArray = new int[numberOfBitsToFlipRoundedDown];
            for (int i = 1; i < numberOfBitsToFlip; i++) {
                flippingBitsArray[i] = IPv4BinBitwise[subnetSlash + i];
            }

        //counter of increments +1 in Decimal
        //counter of increments in binary (+padding)
        //alternatesArray[i][j]  i counts down, j counts across
        for (int i = 0; i < numberOfNetworksMade; i++) {
                counterDec[i] = i;
                counterBinString[i] = Integer.toBinaryString(counterDec[i]);
                counterBinInt[i] = Integer.parseInt(counterBinString[i]);
                counterBinString[i] = String.format("%0" + numberOfBitsToFlipRoundedDown + "d", counterBinInt[i]);
            for (int j = 0; j < numberOfBitsToFlipRoundedDown; j++) {
                alternatesArray[i][j] = Character.getNumericValue(counterBinString[i].charAt(j));
            }
        }

            //feed those one by one back into the IP address to receive the variants (before ID and BA calc)
            for (int i = 0; i < numberOfNetworksDesired; i++) {                      //for every network
                int iPv4DezOctet0 = 0;
                int iPv4DezOctet1 = 0;
                int iPv4DezOctet2 = 0;
                int iPv4DezOctet3 = 0;
                int iPv4DezOctet0BA = 0;
                int iPv4DezOctet1BA = 0;
                int iPv4DezOctet2BA = 0;
                int iPv4DezOctet3BA = 0;
                for (int j = 0; j < numberOfBitsToFlipRoundedDown; j++) {               //for every Bit
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
        //Uneven number of Networks goes here. We make more nets than we output
       else {
        int numberOfNetworksMade = (int)Math.pow(2,numberOfBitsToFlipRoundedUp);
        int[] counterDec = new int[numberOfNetworksMade];
        String[] counterBinString = new String[numberOfNetworksMade];
        int[] counterBinInt = new int[numberOfNetworksMade * numberOfBitsToFlipRoundedUp];
        int[][] alternatesArray2 = new int[numberOfNetworksMade][numberOfBitsToFlipRoundedUp];              //bits to flip rounded up

            for (int i = 0; i < numberOfNetworksMade; i++) {
                counterDec[i] = i;
                counterBinString[i] = Integer.toBinaryString(counterDec[i]);
                counterBinInt[i] = Integer.parseInt(counterBinString[i]);
                counterBinString[i] = String.format("%0" + numberOfBitsToFlipRoundedUp +"d", counterBinInt[i]);

                for (int j = 0; j < numberOfBitsToFlipRoundedUp; j++) {
                    alternatesArray2[i][j] = Character.getNumericValue(counterBinString[i].charAt(j));
                }
            }

            for (int i = 0; i < numberOfNetworksDesired; i++) {                                 //for every network
                for (int j = 0; j < numberOfBitsToFlipRoundedUp; j++) {               //for every Bit
                    IPv4BinBitwise[subnetSlash + j] = alternatesArray2[i][j];
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

                int counter = 0;
                counter ++;

                if (counter < numberOfNetworksDesired) {
                    System.out.println("**===================**"
                            + "\r\n" + "ID: " + iPv4DezOctet0 + "." + iPv4DezOctet1 + "." + iPv4DezOctet2 + "." + iPv4DezOctet3
                            + "\r\n" + "BA: " + iPv4DezOctet0BA + "." + iPv4DezOctet1BA + "." + iPv4DezOctet2BA + "." + iPv4DezOctet3BA
                            + "\r\n" + "**===================**");
                }
            }
            }
        }

    //Networks of different sizes
    public static void differentSizeNetworks() {
        Scanner in = new Scanner(System.in);
        int[] nearestPowerOfTwo = new int[numberOfNetworksDesired];
        int numberOfTotalHosts = 0;
        int numberOfTotalHostsPossible = 0;
        String IPv4BinStringAll = "";
        int[] IPv4BinBitwise = new int[32];
        int[] IPv4BinBitwiseBA = new int[32];

        //get the number of hosts and turn into numberOfHosts[] per Network desired
        for (int i = 0; i < numberOfNetworksDesired; i++) {
            System.out.println("Enter the number of hosts for Network " + (i+1));
            numberOfHosts[i] = in.nextInt() +2;       //+2 für ID und BA
            nearestPowerOfTwo[i] = (int)pow(2, ceil(log(numberOfHosts[i])/log(2)));
            numberOfHosts[i] = nearestPowerOfTwo[i];
        }

        //sort the number of hosts largest to smallest
            List<Integer> numberOfHostList = Arrays.asList(numberOfHosts);
            Collections.sort(numberOfHostList);
            Collections.reverse(numberOfHostList);
            for(int i = 0; i < numberOfHostList.size(); i++){
                numberOfHosts[i]  = numberOfHostList.get(i);
            }

        /*
            //check to see if some hosts are the same
        boolean networksAreSameSize = false;
        for(int i = 0; i < numberOfNetworksDesired +1; i++) {
            if (numberOfHosts[i].equals(numberOfHosts[i + 1])) {
                networksAreSameSize = true;
                System.out.println("Some of these networks are of the same size");
            }
        }
         */

        //total of entered hosts, compare to number of possible hosts
        for (int i = 0; i < numberOfNetworksDesired; i++){
            numberOfTotalHosts = numberOfTotalHosts + numberOfHosts[i];
        }
        numberOfTotalHostsPossible = (int)Math.pow(2, 32-subnetSlash);
        if(numberOfTotalHosts <= numberOfTotalHostsPossible) {

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
            for (int i = 0; i < numberOfNetworksDesired; i++) {
                //subnetzSlash      <-starting subnetmask
                //Determine new subnetzmask
                newSubnetSlash = subnetSlash + (log(numberOfTotalHostsPossible/numberOfHosts[i])/log(2));

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
                        +"\r\n" +"Hosts:             " +(numberOfHosts[i] -2)
                        +"\r\n" +"**==============================**"
                );
            }

            //Fehler für zuviele Hosts
        }else{
            System.out.println("I'm sorry but that is too many hosts \\" +subnetSlash +" Netz.");
        }

    }
}

class IPv6 {
    //IP Adress
    static String[] IPv6Hex = {"abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"};
    static String[] IPv6BinString = {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000"};
    static int[] IPv6BinInt = {0, 0, 0, 0, 0, 0, 0, 0};

    //Subnetmask
    static int subnetSlash = 0;
    static double newSubnetSlash = 0;
    static int numberOfNetworksDesired;

    //Constructor
    IPv6() {
    }

    public static void encoding() {
        Scanner in = new Scanner(System.in);
//input IP
        System.out.println("Welcome to this IPv6 Calculator");
        System.out.println("First the IP-Adress, in longform please:");
        System.out.println("Please enter the first octet:");
        IPv6Hex[0] = in.nextLine();
        System.out.println("Please enter the second octet:");
        IPv6Hex[1] = in.nextLine();
        System.out.println("Please enter the third octet:");
        IPv6Hex[2] = in.nextLine();
        System.out.println("Please enter the fourth octet:");
        IPv6Hex[3] = in.nextLine();
        System.out.println("Please enter the fifth octet:");
        IPv6Hex[4] = in.nextLine();
        System.out.println("Please enter the sixth octet:");
        IPv6Hex[5] = in.nextLine();
        System.out.println("Please enter the seventh octet:");
        IPv6Hex[6] = in.nextLine();
        System.out.println("Please enter the eights octet:");
        IPv6Hex[7] = in.nextLine();

//converts to binary
        for (int i = 0; i < 8; i++) {
            IPv6BinString[i] = hex.hexToBin(IPv6Hex[i]);
        }

//input Subnetmask
        System.out.println("And now the subnetmask please.");
        subnetSlash = in.nextInt();

//collects desired subnets and returns new subnetmask
        String eingabe;
        Scanner in3 = new Scanner(System.in);
        System.out.println("How many networks do you want to make?");
        numberOfNetworksDesired = in3.nextInt();
        double numberOfBitsToFlip = log(numberOfNetworksDesired) / log(2);

        newSubnetSlash = (int)(subnetSlash + numberOfBitsToFlip);
        if(!(numberOfBitsToFlip % 2 == 0)){
            numberOfBitsToFlip = (int)(numberOfBitsToFlip +1);
        }

        int numberOfBitsToFlipRoundedUp = (int) numberOfBitsToFlip + 1;           //the number of subnets we make vs.
        int numberOfBitsToFlipRoundedDown = (int) numberOfBitsToFlip;            //the number of subnets we give out
    }
}

    public class IPv4Calculator {
        public static void main(String[] args) {
                IPv4.encoding();
               IPv4.numberAndSizeOfNetworks();

//gleichgroße Netze:
            if (IPv4.isSameSize) {
                IPv4.sameSizeNetworks();
            }
//ungleiche Netze:
            if (!IPv4.isSameSize) {
                IPv4.differentSizeNetworks();
            }
        }
 

        }
    

