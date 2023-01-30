 /**
* IPv4 Subnetting Calculator
* author: Gisela Wolf
* date: 23.01.2023
* Issues still to fix:
* IPv4
* - Userproofing inputs (number of networks)
* - problem: Same size nets in different size network option
* IPv6
* - calculation
* - output
 */

package src;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.lang.Integer;
import java.lang.String;
import static java.lang.Math.*;

class hex{
    public static @NotNull String hexToBin(String hex){
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
    private final static int[] IPv4Dec = {0, 0, 0, 0};
    static int[] IPv4BinInt = {0, 0, 0, 0};
    static String[] IPv4BinString = {"0", "0", "0", "0"};
    //Subnetmask
    private final static int[] subnetDec = {0, 0, 0, 0};
    static int[] subnetBinInt = {0, 0, 0, 0};
    static String[] subnetBinString = {"0", "0", "0", "0"};

    private static int subnetSlash = 0;
    static double newSubnetSlash = 0;
    private static int numberOfNetworksDesired;
    static boolean isSameSize = true;
    static boolean input = true;
    static boolean input2 = true;

    public static int getIPv4Dec(int index) {
        return IPv4Dec[index];
    }
    public static void setIPv4Dec(int IPv4Dec, int index) {
        if (IPv4Dec > 0 && IPv4Dec < 256) {
            IPv4.IPv4Dec[index] = IPv4Dec;
            input = true;
        } else {
            input = false;
            System.out.println("That is not a valid octet. Please try again");
        }
    }
    public static int getSubnetDec(int index) {
        return subnetDec[index];
    }
    public static void setSubnetDec(int subnetDec, int index) {
        if (subnetDec >= 0 && subnetDec < 256) {
        IPv4.subnetDec[index] = subnetDec;
        input = true;
    } else {
        input = false;
        System.out.println("That is not a valid octet. Please try again");
    }
    }
    public static int getSubnetSlash() {
        return subnetSlash;
    }
    public static void setSubnetSlash(int subnetSlash) {
        if (subnetSlash > 0 && subnetSlash < 33) {
            IPv4.subnetSlash = subnetSlash;
            input = true;
        }else{
            System.out.println("That is not a valid subnetmask. Please try again");
            input = false;
        }
    }
    public static int getNumberOfNetworksDesired() {
        return numberOfNetworksDesired;
    }
    public static void setNumberOfNetworksDesired(int numberOfNetworksDesired) {
        for (int i = 8; i < 22; i++) {
            if (IPv4.getSubnetSlash() == i && getNumberOfNetworksDesired() > (Math.pow(2, i) - 2)) {
                System.out.println("That are too many networks for the subnetmask");
                input2 = false;
            }
        }
        if (input) {
            IPv4.numberOfNetworksDesired = numberOfNetworksDesired;
        }
    }

    //Constructor
    IPv4() {
    }

    //collects IP and subnetmask and converts them to binary and slash-notation
    public static void encoding() {
        //input IP
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to this IPv4 Calculator");
        System.out.println("First the IP-Adress:");
            for (int i = 1; i < 5; i++) {
                while(true) {
                    System.out.println("Please enter the " + i + ". octet:");
                    setIPv4Dec(in.nextInt(), i - 1);
                    if(input){break;}
                }
            }

//converts to binary
        for (int i = 0; i < IPv4Dec.length; i++) {
            IPv4BinInt[i] = Integer.parseInt(Integer.toBinaryString(getIPv4Dec(i)));
            IPv4BinString[i] = Integer.toBinaryString(getIPv4Dec(i));
            if (IPv4BinString[i].length() < 8) {
                IPv4BinString[i] = String.format("%08d", IPv4BinInt[i]);
            }
        }

//input Subnetmask
        System.out.println("And now the subnetmask please. Do you wish to enter it in octets (O) or slash-notation (S)? ");
        Scanner in2 = new Scanner(System.in);
        String octetOrSlash = in2.nextLine();

        if(octetOrSlash.equals("O") || octetOrSlash.equals("o")) {
            for (int i = 1; i < 5; i++) {
                while(true){
                    System.out.println("Please enter the " + i + ". octet:");
                    setSubnetDec(in.nextInt(), i - 1);
                    if(input){break;}
                }
            }

        }else if(octetOrSlash.equals("S") || octetOrSlash.equals("s")) {
            while (true) {
                System.out.println("Please enter the \\subnetmask");
                setSubnetSlash(in2.nextInt());
                if (input) {
                    break;
                }
            }
        }
//converts to binary and then to /-notation
        for (int i = 0; i < subnetDec.length; i++) {
            subnetBinString[i] = Integer.toBinaryString(getSubnetDec(i));
            subnetBinInt[i] = Integer.parseInt(Integer.toBinaryString(getSubnetDec(i)));
            setSubnetSlash(getSubnetSlash() + Integer.bitCount(getSubnetDec(i)));

            if (subnetBinString[i].length() < 8) {
                subnetBinString[i] = String.format("%08d", subnetBinInt[i]);      //padding
            }
        }
    }

    // query for number and size of subnets
    public static void numberAndSizeOfNetworks() {                                //collects desired subnets and returns new subnetmask, ID and BA and number of hosts
        String eingabe;
        while(true) {
            System.out.println("How many networks do you want to make?");
            Scanner in3 = new Scanner(System.in);
            setNumberOfNetworksDesired(in3.nextInt());
            if(input2){break;}
        }
        System.out.println("debug: " +input);

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
        double numberOfBitsToFlip = log(getNumberOfNetworksDesired()) / log(2);
        int numberOfBitsToFlipRoundedUp = (int) numberOfBitsToFlip + 1;           //the number of subnets we make
        int numberOfBitsToFlipRoundedDown = (int) numberOfBitsToFlip;            //the number of subnets we give out

        //this is where the magic of subnetting happens
        //This is the good one. Number of bits is even and we proceed to make all the subnets we output
        int[][] iPv4BinOctetwise = new int[0][];
    if (numberOfBitsToFlip == numberOfBitsToFlipRoundedDown) {
        int numberOfNetworksMade = getNumberOfNetworksDesired();
        int[] counterDec = new int[numberOfNetworksMade];
        String[] counterBinString = new String[numberOfNetworksMade];
        int[] counterBinInt = new int[numberOfNetworksMade * numberOfBitsToFlipRoundedDown];
        int[][] alternatesArray = new int[numberOfNetworksMade][numberOfBitsToFlipRoundedDown];             //bits to flip even

            //make array out of the bits we need to increment
            int[] flippingBitsArray = new int[numberOfBitsToFlipRoundedDown];
            for (int i = 1; i < numberOfBitsToFlip; i++) {
                flippingBitsArray[i] = IPv4BinBitwise[getSubnetSlash() + i];
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
            for (int i = 0; i < getNumberOfNetworksDesired(); i++) {                      //for every network
                int iPv4DezOctet0 = 0;
                int iPv4DezOctet1 = 0;
                int iPv4DezOctet2 = 0;
                int iPv4DezOctet3 = 0;
                int iPv4DezOctet0BA = 0;
                int iPv4DezOctet1BA = 0;
                int iPv4DezOctet2BA = 0;
                int iPv4DezOctet3BA = 0;
                for (int j = 0; j < numberOfBitsToFlipRoundedDown; j++) {               //for every Bit
                    IPv4BinBitwise[getSubnetSlash() + j] = alternatesArray[i][j];

                //ID
                    //All host bits to zero
                    for (int x = getSubnetSlash() + (int)numberOfBitsToFlip; x < 32; x++) {
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
                    for (int x = getSubnetSlash() +((int)numberOfBitsToFlip); x < 32; x++) {
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

            for (int i = 0; i < getNumberOfNetworksDesired(); i++) {                                 //for every network
                for (int j = 0; j < numberOfBitsToFlipRoundedUp; j++) {                             //for every Bit
                    IPv4BinBitwise[getSubnetSlash() + j] = alternatesArray2[i][j];
                }

                //ID
                //All host bits to zero
                for (int x = getSubnetSlash() + numberOfBitsToFlipRoundedUp; x < 32; x++){
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
                for (int x = getSubnetSlash() + numberOfBitsToFlipRoundedUp; x < 32; x++) {
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

                if (counter < getNumberOfNetworksDesired()) {
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
        int[] nearestPowerOfTwo = new int[getNumberOfNetworksDesired()];
        int numberOfTotalHosts = 0;
        int numberOfTotalHostsPossible = 0;
        String IPv4BinStringAll = "";
        int[] IPv4BinBitwise = new int[32];
        int[] IPv4BinBitwiseBA = new int[32];
        Integer[] numberOfHosts = new Integer[numberOfNetworksDesired];

        //get the number of hosts and turn into numberOfHosts[] per Network desired
        for (int i = 0; i < getNumberOfNetworksDesired(); i++) {
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
        for (int i = 0; i < getNumberOfNetworksDesired(); i++){
            numberOfTotalHosts = numberOfTotalHosts + numberOfHosts[i];
        }
        numberOfTotalHostsPossible = (int)Math.pow(2, 32-getSubnetSlash());
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
            for (int i = getSubnetSlash(); i < 32; i++){
                IPv4BinBitwise[i] = 0;
            }

            //here starts the big for loop, one for each new subnet
            for (int i = 0; i < getNumberOfNetworksDesired(); i++) {
                //subnetzSlash      <-starting subnetmask
                //Determine new subnetzmask
                newSubnetSlash = getSubnetSlash() + (log(numberOfTotalHostsPossible/numberOfHosts[i])/log(2));

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
            System.out.println("I'm sorry but that is too many hosts \\" +getSubnetSlash() +" Netz.");
        }
    }
}

class IPv6 {
    //IP Adress
    private static String[] IPv6Hex = {"abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd", "abcd"};
    static String[] IPv6BinString = {"0000", "0000", "0000", "0000", "0000", "0000", "0000", "0000"};
    static int[] IPv6BinInt = {0, 0, 0, 0, 0, 0, 0, 0};
    static boolean input = true;
    static boolean input2 = false;

    public static String getIPv6Hex(int index) {
        return IPv6Hex[index];
    }

    public static void setIPv6Hex(String IPv6Hex, int index) {

        //input too short
        if (String.valueOf(IPv6Hex).length() < 4) {
            IPv6.IPv6Hex[index] = String.format("%1$" + 4 + "s", IPv6Hex).replace(' ', '0');
        } else {
            IPv6.IPv6Hex[index] = String.valueOf(IPv6Hex);
        }
    }

    //Subnetmask
    private static int subnetSlash = 0;
    static double newSubnetSlash = 0;
    private static int numberOfNetworksDesired;

    public static int getSubnetSlash() {
        return subnetSlash;
    }

    public static void setSubnetSlash(int subnetSlash) {
        IPv6.subnetSlash = subnetSlash;
    }

    public static int getNumberOfNetworksDesired() {
        return numberOfNetworksDesired;
    }

    public static void setNumberOfNetworksDesired(int numberOfNetworksDesired) {
        IPv6.numberOfNetworksDesired = numberOfNetworksDesired;
    }

    //Constructor
    IPv6() {
    }

    //encoding complete, ADD SHORTFORM INPUT
    public static void encoding() {
        char[] hexChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F'};

        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to this IPv6 Calculator");
        System.out.println("First the IP-Adress.");
        boolean input3 = true;
        String eingabe;

//input IP
        //test input for length and correct chars
        for (int i = 0; i < 8; i++) {
            do {
                do {
                    System.out.println("Please enter the " + (i + 1) + ". quartet:");
                    eingabe = in.nextLine();

                    if (eingabe.isEmpty()) {
                        eingabe = "0000";
                    }
                    boolean[] eingabeBool = new boolean[eingabe.length()];

                    //test the hex input
                    //wrong letters
                    for (int j = 0; j < eingabe.length(); j++) {
                        for (int k = 0; k < 22; k++) {
                            if (eingabe.charAt(j) == hexChar[k]) {
                                eingabeBool[j] = true;
                            }
                        }
                    }
                    for (int x = 0; x < eingabe.length(); x++) {
                        if (!eingabeBool[x]) {
                            System.out.println("That is not a valid hex number");
                            input3 = false;
                        } else {
                            input3 = true;
                        }
                    }
                } while (!input3);
                //too long
                if (eingabe.length() > 4) {
                    System.out.println("That number is too long.");
                    input2 = false;
                } else {
                    setIPv6Hex(eingabe, i);
                    input2 = true;
                    break;
                }
            } while (!input2);
        }

//converts to binary
        for (int i = 0; i < 8; i++) {
            IPv6BinString[i] = hex.hexToBin(getIPv6Hex(i));
        }

//input Subnetmask
        System.out.println("And now the subnetmask please.");
        setSubnetSlash(in.nextInt());

//collects desired subnets and returns new subnetmask
        Scanner in3 = new Scanner(System.in);
        System.out.println("How many networks do you want to make?");
        setNumberOfNetworksDesired(in3.nextInt());
        double numberOfBitsToFlip = log(getNumberOfNetworksDesired()) / log(2);

        newSubnetSlash = (int) (getSubnetSlash() + numberOfBitsToFlip);
        if (!(numberOfBitsToFlip % 2 == 0)) {
            numberOfBitsToFlip = (int) (numberOfBitsToFlip + 1);
        }

        int numberOfBitsToFlipRoundedUp = (int) numberOfBitsToFlip + 1;           //the number of subnets we make vs.
        int numberOfBitsToFlipRoundedDown = (int) numberOfBitsToFlip;            //the number of subnets we give out
    }

    public static void numberOfNetworks() {                                //collects desired subnets and returns new subnetmask, ID and BA and number of hosts
        String eingabe;
        while (true) {
            System.out.println("How many networks do you want to make?");
            Scanner in3 = new Scanner(System.in);
            setNumberOfNetworksDesired(in3.nextInt());
            if (input2) {
                break;
            }
        }
        System.out.println();



        System.out.println("Debug:------------------------"
                +"\r\n"  +"Hex String: "    + Arrays.toString(IPv6Hex)
                +"\r\n"  +"Binary String: " + Arrays.toString(IPv6BinString)
                +"\r\n"  +"Subnetmask: "    + newSubnetSlash
                +"\r\n"  +"Number of Nets: "+ getNumberOfNetworksDesired()
                +"\r\n"  +"-------------------------------");


    }
}

    public class IPv4Calculator {
        public static void main(String[] args) {
            boolean input = false;

            while (!input) {
                System.out.println("IPv4 oder IPv6? (4/6)");
                Scanner in = new Scanner(System.in);
                String eingabe = in.nextLine();
//IPv6
                if (eingabe.equals("6")) {
                    input = true;
                    IPv6.encoding();
                    IPv6.numberOfNetworks();
//IPv4
                } else if (eingabe.equals("4")) {
                    input = true;
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
                } else {
                    input = false;
                }
            }
        }
    }


