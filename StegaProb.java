//package StegaProb;

import FrameViewer.FrameViewer;
import Utilities.BinaryToDecimal;
import Utilities.ByteWriter;
import Utilities.BytesToBinary;
import Utilities.FrameCharacteristics;
import domain.Frame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class StegaProb{

    public static void main(String[] args) {
    /*	
      //      Here we are selecting the file to analyze
		JFileChooser fileChooser = new JFileChooser();	
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(null);
      if (result == JFileChooser.APPROVE_OPTION) {
         File mp3SelectedFile = fileChooser.getSelectedFile();
         System.out.println("Selected file: " + mp3SelectedFile.getAbsolutePath());
      } 
      
       //      Here we want to read in the selected file & return basic information
      
      FileInputStream fileInput = null;	      
      int i = 0;
      File mp3SelectedFile = fileChooser.getSelectedFile();
      ArrayList fileArray = new ArrayList ();
      
      fileInput = new FileInputStream(mp3SelectedFile);
      
      i= fileInput.read(fileArray);
      
      System.out.println("Initial size of File: " + fileArray.size());       
       
       if(fileInput !=null){
      	 fileInput.close();
       }*/
      
    	/* ArrayList<byte[]> data = new ArrayList<byte[]>();
         FileInputStream in = new FileInputStream( args[0] );
         byte[] buffer = new byte[ 128 ];
         int length;
         int total = 0;
         while( ( length = in.read(buffer)) != -1 ) {
             byte[] bytes = new byte[ length ];
             System.arraycopy( buffer, 0, bytes, 0, length);
             data.add(bytes);
             total += length;
         }
         byte[] result = new byte[ total ];
         int nn = 0;
         for( byte[] entry : data ) {
             System.arraycopy( entry, 0, result, nn, entry.length );
             nn += entry.length;
         }*/
    	
    	
    	
        if (args.length != 0) {
            File filePath;
            byte[] fileByteArray;
            boolean scanHeaderBitsFlag = false;
            boolean scanSideInformationBitsFlag = false;
            boolean scanEmptyFrameBitsWithoutAncillaryFlag = false;
            boolean scanEmptyFrameBitsWithAncillaryFlag = false;
            boolean scanAncillaryBitsFlag = false;
            boolean frameViewerFlag = false;
            List<String> argList = Arrays.asList(args);
            ArrayList<String> argArrayList = new ArrayList<String>(argList);
            if (argArrayList.contains("-h") || argArrayList.contains("-c")) {
                scanHeaderBitsFlag = true;
                argArrayList.remove("-h");
            }
            if (argArrayList.contains("-s") || argArrayList.contains("-c")) {
                scanSideInformationBitsFlag = true;
                argArrayList.remove("-s");
            }
            if (argArrayList.contains("-f") || argArrayList.contains("-c")) {
                scanEmptyFrameBitsWithoutAncillaryFlag = true;
                argArrayList.remove("-f");
            }
            if (argArrayList.contains("-fa") || argArrayList.contains("-c")) {
                scanEmptyFrameBitsWithAncillaryFlag = true;
                argArrayList.remove("-fa");
            }
            if (argArrayList.contains("-a") || argArrayList.contains("-c")) {
                scanAncillaryBitsFlag = true;
                argArrayList.remove("-a");
            }
            if (argArrayList.contains("-c")) {
                argArrayList.remove("-c");
            }
            if (argArrayList.contains("-fv")) {
                frameViewerFlag = true;
                argArrayList.remove("-fv");
            }
            
            if ((scanHeaderBitsFlag || scanSideInformationBitsFlag || scanEmptyFrameBitsWithoutAncillaryFlag || scanEmptyFrameBitsWithAncillaryFlag || scanAncillaryBitsFlag || frameViewerFlag) && !argArrayList.isEmpty() && (fileByteArray = Utilities.FileReader.getFileBytes(filePath = new File(argArrayList.get(0)))) != null) {
                ArrayList<Frame> frameArrayList = StegaProb.getFrameArrayList(fileByteArray);
                fileByteArray = null;
                if (frameViewerFlag) {
                    FrameViewer.frameViewerMenu(frameArrayList, filePath);
                } else {
                    if (scanHeaderBitsFlag) {
                        StegaProb.scanHeaderBitsCMD(frameArrayList, filePath);
                    }
                    if (scanSideInformationBitsFlag) {
                        StegaProb.scanSideInformationBitsCMD(frameArrayList, filePath);
                    }
                    if (scanEmptyFrameBitsWithoutAncillaryFlag) {
                        StegaProb.scanEmptyFrameBitsWithoutAncillaryCMD(frameArrayList, filePath);
                    }
                    if (scanEmptyFrameBitsWithAncillaryFlag) {
                        StegaProb.scanEmptyFrameBitsWithAncillaryCMD(frameArrayList, filePath);
                    }
                    if (scanAncillaryBitsFlag) {
                        StegaProb.scanAncillaryBitsCMD(frameArrayList, filePath);
                    }
                }
            }
        };
    }

    private static ArrayList<Frame> getFrameArrayList(byte[] fileByteArray) {
        ArrayList<Frame> frameArrayList = new ArrayList<Frame>();
        int id = 0;
        int frameIndex = 0;
        String mpegAudioVersion = "";
        String layer = "";
        boolean protectionBitSet = false;
        double bitRate = 0.0;
        double samplingFrequency = 0.0;
        boolean paddingBitSet = false;
        boolean privateBitSet = false;
        String channelMode = "";
        String modeExtension = "";
        boolean copyrightBitSet = false;
        boolean originalBitSet = false;
        boolean emphasisBit1Set = false;
        boolean emphasisBit2Set = false;
        String emphasisType = "";
        int frameLength = 0;
        int main_data_beginBytes = 0;
        int part2_3_lengthBits1 = 0;
        int part2_3_lengthBits2 = 0;
        int part2_3_lengthBits3 = 0;
        int part2_3_lengthBits4 = 0;
        int big_valuesLengthBits1 = 0;
        int big_valuesLengthBits2 = 0;
        int big_valuesLengthBits3 = 0;
        int big_valuesLengthBits4 = 0;
        boolean winSwitchFlagBitSet1 = false;
        boolean winSwitchFlagBitSet2 = false;
        boolean winSwitchFlagBitSet3 = false;
        boolean winSwitchFlagBitSet4 = false;
        int table_selectBits1 = 0;
        int table_selectBits2 = 0;
        int table_selectBits3 = 0;
        int table_selectBits4 = 0;
        boolean specialFrameFlag = false;
        boolean emptyFrameFlag = false;
        boolean incrementFirst = false;
        do {
            frameIndex = StegaProb.findFirstFrameIndex(fileByteArray, frameIndex, incrementFirst);
            bitRate = FrameCharacteristics.getBitRate(fileByteArray, frameIndex);
            samplingFrequency = FrameCharacteristics.getSamplingFrequency(fileByteArray, frameIndex);
            paddingBitSet = (fileByteArray[frameIndex + 2] & 2) != 0;
            frameLength = FrameCharacteristics.getFrameLength(bitRate, samplingFrequency, paddingBitSet);
            if (fileByteArray[frameIndex + frameLength] == -1 && fileByteArray[frameIndex + frameLength + 1] == -6 || fileByteArray[frameIndex + frameLength] == -1 && fileByteArray[frameIndex + frameLength + 1] == -5) break;
            incrementFirst = true;
        } while (true);
        while (frameIndex < fileByteArray.length && (fileByteArray[frameIndex] == -1 && fileByteArray[frameIndex + 1] == -6 || fileByteArray[frameIndex] == -1 && fileByteArray[frameIndex + 1] == -5)) {
            int a;
            mpegAudioVersion = "";
            layer = "";
            protectionBitSet = false;
            bitRate = 0.0;
            samplingFrequency = 0.0;
            paddingBitSet = false;
            privateBitSet = false;
            channelMode = "";
            modeExtension = "";
            copyrightBitSet = false;
            originalBitSet = false;
            emphasisBit1Set = false;
            emphasisBit2Set = false;
            emphasisType = "";
            frameLength = 0;
            ArrayList<Byte> frameBytes = new ArrayList<Byte>();
            ArrayList<Byte> headerBytes = new ArrayList<Byte>();
            ArrayList<Byte> crcBytes = new ArrayList<Byte>();
            ArrayList<Byte> sideInformationBytes = new ArrayList<Byte>();
            ArrayList<Byte> mainDataBytes = new ArrayList<Byte>();
            main_data_beginBytes = 0;
            part2_3_lengthBits1 = 0;
            part2_3_lengthBits2 = 0;
            part2_3_lengthBits3 = 0;
            part2_3_lengthBits4 = 0;
            big_valuesLengthBits1 = 0;
            big_valuesLengthBits2 = 0;
            big_valuesLengthBits3 = 0;
            big_valuesLengthBits4 = 0;
            winSwitchFlagBitSet1 = false;
            winSwitchFlagBitSet2 = false;
            winSwitchFlagBitSet3 = false;
            winSwitchFlagBitSet4 = false;
            table_selectBits1 = 0;
            table_selectBits2 = 0;
            table_selectBits3 = 0;
            table_selectBits4 = 0;
            specialFrameFlag = false;
            emptyFrameFlag = false;
            mpegAudioVersion = FrameCharacteristics.getMPEGAudioVersion(fileByteArray, frameIndex);
            layer = FrameCharacteristics.getLayer(fileByteArray, frameIndex);
            protectionBitSet = (fileByteArray[frameIndex + 1] & 1) != 0;
            bitRate = FrameCharacteristics.getBitRate(fileByteArray, frameIndex);
            samplingFrequency = FrameCharacteristics.getSamplingFrequency(fileByteArray, frameIndex);
            paddingBitSet = (fileByteArray[frameIndex + 2] & 2) != 0;
            privateBitSet = (fileByteArray[frameIndex + 2] & 1) != 0;
            channelMode = FrameCharacteristics.getChannelMode(fileByteArray, frameIndex);
            modeExtension = FrameCharacteristics.getModeExtension(fileByteArray, frameIndex);
            copyrightBitSet = (fileByteArray[frameIndex + 3] & 8) != 0;
            originalBitSet = (fileByteArray[frameIndex + 3] & 4) != 0;
            emphasisBit1Set = (fileByteArray[frameIndex + 3] & 2) != 0;
            emphasisBit2Set = (fileByteArray[frameIndex + 3] & 1) != 0;
            emphasisType = FrameCharacteristics.getEmphasisType(fileByteArray, frameIndex);
            frameLength = FrameCharacteristics.getFrameLength(bitRate, samplingFrequency, paddingBitSet);
            if (frameLength > fileByteArray.length - frameIndex) {
                specialFrameFlag = true;
                for (a = frameIndex; a < fileByteArray.length; ++a) {
                    frameBytes.add(Byte.valueOf(fileByteArray[a]));
                }
            } else {
                for (a = 0; a < frameLength; ++a) {
                    frameBytes.add(Byte.valueOf(fileByteArray[frameIndex + a]));
                }
            }
            for (a = 0; a < 4; ++a) {
                headerBytes.add(Byte.valueOf(fileByteArray[frameIndex + a]));
            }
            if (channelMode.equals("Single Channel")) {
                if (protectionBitSet) {
                    for (a = 4; a < 21; ++a) {
                        sideInformationBytes.add(Byte.valueOf(fileByteArray[frameIndex + a]));
                    }
                    if (specialFrameFlag) {
                        for (a = 21; a < fileByteArray.length - frameIndex; ++a) {
                            mainDataBytes.add(Byte.valueOf(fileByteArray[frameIndex + a]));
                        }
                    } else {
                        for (a = 21; a < frameLength; ++a) {
                            mainDataBytes.add(Byte.valueOf(fileByteArray[frameIndex + a]));
                        }
                    }
                } else {
                    for (a = 4; a < 6; ++a) {
                        crcBytes.add(Byte.valueOf(fileByteArray[frameIndex + a]));
                    }
                    for (a = 6; a < 23; ++a) {
                        sideInformationBytes.add(Byte.valueOf(fileByteArray[frameIndex + a]));
                    }
                    if (specialFrameFlag) {
                        for (a = 23; a < fileByteArray.length - frameIndex; ++a) {
                            mainDataBytes.add(Byte.valueOf(fileByteArray[frameIndex + a]));
                        }
                    } else {
                        for (a = 23; a < frameLength; ++a) {
                            mainDataBytes.add(Byte.valueOf(fileByteArray[frameIndex + a]));
                        }
                    }
                }
                part2_3_lengthBits1 = FrameCharacteristics.getPart2_3_LengthBitsSingle1(sideInformationBytes);
                part2_3_lengthBits2 = FrameCharacteristics.getPart2_3_LengthBitsSingle2(sideInformationBytes);
                big_valuesLengthBits1 = FrameCharacteristics.getBig_ValuesLengthBitsSingle1(sideInformationBytes);
                big_valuesLengthBits2 = FrameCharacteristics.getBig_ValuesLengthBitsSingle2(sideInformationBytes);
                if ((sideInformationBytes.get(6).byteValue() & 16) != 0) {
                    winSwitchFlagBitSet1 = true;
                }
                if ((sideInformationBytes.get(13).byteValue() & 2) != 0) {
                    winSwitchFlagBitSet2 = true;
                }
                table_selectBits1 = FrameCharacteristics.getTable_SelectBitsSingle1(sideInformationBytes, winSwitchFlagBitSet1);
                table_selectBits2 = FrameCharacteristics.getTable_SelectBitsSingle2(sideInformationBytes, winSwitchFlagBitSet2);
            } else {
                if (protectionBitSet) {
                    for (a = 4; a < 36; ++a) {
                        sideInformationBytes.add(Byte.valueOf(fileByteArray[frameIndex + a]));
                    }
                    if (specialFrameFlag) {
                        for (a = 36; a < fileByteArray.length - frameIndex; ++a) {
                            mainDataBytes.add(Byte.valueOf(fileByteArray[frameIndex + a]));
                        }
                    } else {
                        for (a = 36; a < frameLength; ++a) {
                            mainDataBytes.add(Byte.valueOf(fileByteArray[frameIndex + a]));
                        }
                    }
                } else {
                    for (a = 4; a < 6; ++a) {
                        crcBytes.add(Byte.valueOf(fileByteArray[frameIndex + a]));
                    }
                    for (a = 6; a < 38; ++a) {
                        sideInformationBytes.add(Byte.valueOf(fileByteArray[frameIndex + a]));
                    }
                    if (specialFrameFlag) {
                        for (a = 38; a < fileByteArray.length - frameIndex; ++a) {
                            mainDataBytes.add(Byte.valueOf(fileByteArray[frameIndex + a]));
                        }
                    } else {
                        for (a = 38; a < frameLength; ++a) {
                            mainDataBytes.add(Byte.valueOf(fileByteArray[frameIndex + a]));
                        }
                    }
                }
                part2_3_lengthBits1 = FrameCharacteristics.getPart2_3_LengthBitsDual1(sideInformationBytes);
                part2_3_lengthBits2 = FrameCharacteristics.getPart2_3_LengthBitsDual2(sideInformationBytes);
                part2_3_lengthBits3 = FrameCharacteristics.getPart2_3_LengthBitsDual3(sideInformationBytes);
                part2_3_lengthBits4 = FrameCharacteristics.getPart2_3_LengthBitsDual4(sideInformationBytes);
                big_valuesLengthBits1 = FrameCharacteristics.getBig_ValuesLengthBitsDual1(sideInformationBytes);
                big_valuesLengthBits2 = FrameCharacteristics.getBig_ValuesLengthBitsDual2(sideInformationBytes);
                big_valuesLengthBits3 = FrameCharacteristics.getBig_ValuesLengthBitsDual3(sideInformationBytes);
                big_valuesLengthBits4 = FrameCharacteristics.getBig_ValuesLengthBitsDual4(sideInformationBytes);
                if ((sideInformationBytes.get(6).byteValue() & 4) != 0) {
                    winSwitchFlagBitSet1 = true;
                }
                if ((sideInformationBytes.get(14).byteValue() & 128) != 0) {
                    winSwitchFlagBitSet2 = true;
                }
                if ((sideInformationBytes.get(21).byteValue() & 16) != 0) {
                    winSwitchFlagBitSet3 = true;
                }
                if ((sideInformationBytes.get(28).byteValue() & 2) != 0) {
                    winSwitchFlagBitSet4 = true;
                }
                table_selectBits1 = FrameCharacteristics.getTable_SelectBitsDual1(sideInformationBytes, winSwitchFlagBitSet1);
                table_selectBits2 = FrameCharacteristics.getTable_SelectBitsDual2(sideInformationBytes, winSwitchFlagBitSet2);
                table_selectBits3 = FrameCharacteristics.getTable_SelectBitsDual3(sideInformationBytes, winSwitchFlagBitSet3);
                table_selectBits4 = FrameCharacteristics.getTable_SelectBitsDual4(sideInformationBytes, winSwitchFlagBitSet4);
            }
            if (big_valuesLengthBits1 + big_valuesLengthBits2 + big_valuesLengthBits3 + big_valuesLengthBits4 + table_selectBits1 + table_selectBits2 + table_selectBits3 + table_selectBits4 == 0) {
                emptyFrameFlag = true;
            }
            main_data_beginBytes = FrameCharacteristics.getMain_Data_BeginBytes(sideInformationBytes);
            frameArrayList.add(new Frame(id, frameIndex, mpegAudioVersion, layer, protectionBitSet, bitRate, samplingFrequency, paddingBitSet, privateBitSet, channelMode, modeExtension, copyrightBitSet, originalBitSet, emphasisBit1Set, emphasisBit2Set, emphasisType, frameLength, frameBytes, headerBytes, crcBytes, sideInformationBytes, mainDataBytes, main_data_beginBytes, part2_3_lengthBits1, part2_3_lengthBits2, part2_3_lengthBits3, part2_3_lengthBits4, big_valuesLengthBits1, big_valuesLengthBits2, big_valuesLengthBits3, big_valuesLengthBits4, winSwitchFlagBitSet1, winSwitchFlagBitSet2, winSwitchFlagBitSet3, winSwitchFlagBitSet4, table_selectBits1, table_selectBits2, table_selectBits3, table_selectBits4, specialFrameFlag, emptyFrameFlag));
            frameIndex += frameLength;
            ++id;
        }
        return frameArrayList;
    }

    private static int findFirstFrameIndex(byte[] fileByteArray, int frameIndex, boolean incrementFirst) {
        if (incrementFirst) {
            ++frameIndex;
        }
        try {
            while (!(fileByteArray[frameIndex] == -1 && fileByteArray[frameIndex + 1] == -6 || fileByteArray[frameIndex] == -1 && fileByteArray[frameIndex + 1] == -5)) {
                ++frameIndex;
            }
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("\n\n** Error: Invalid file selected! Please select a valid MP3 file. **\n");
            System.exit(0);
        }
        return frameIndex;
    }

    private static void scanHeaderBitsCMD(ArrayList<Frame> frameArrayList, File filePath) {
        int a;
        int numberOfBitsToBeWritten;
        int privateBitStateChangeCounter = 0;
        int copyrightBitStateChangeCounter = 0;
        int originalBitStateChangeCounter = 0;
        int emphasisBit1StateChangeCounter = 0;
        int emphasisBit2StateChangeCounter = 0;
        int totalBitStateChanges = 0;
        StringBuilder pBits = new StringBuilder();
        StringBuilder pcBits = new StringBuilder();
        StringBuilder pcoBits = new StringBuilder();
        StringBuilder pcoe1Bits = new StringBuilder();
        StringBuilder pcoe1e2Bits = new StringBuilder();
        String foundFileString = "No files were found within the extracted Frame Header information.";
        for (Frame frame : frameArrayList) {
            if (frame.getId() == frameArrayList.size() - 1) continue;
            if (frame.isPrivateBitSet() != frameArrayList.get(frame.getId() + 1).isPrivateBitSet()) {
                ++privateBitStateChangeCounter;
            }
            if (frame.isCopyrightBitSet() != frameArrayList.get(frame.getId() + 1).isCopyrightBitSet()) {
                ++copyrightBitStateChangeCounter;
            }
            if (frame.isOriginalBitSet() != frameArrayList.get(frame.getId() + 1).isOriginalBitSet()) {
                ++originalBitStateChangeCounter;
            }
            if (frame.isEmphasisBit1Set() != frameArrayList.get(frame.getId() + 1).isEmphasisBit1Set()) {
                ++emphasisBit1StateChangeCounter;
            }
            if (frame.isEmphasisBit2Set() != frameArrayList.get(frame.getId() + 1).isEmphasisBit2Set()) {
                ++emphasisBit2StateChangeCounter;
            }
            if (frame.isPrivateBitSet()) {
                pBits.append("1");
                pcBits.append("1");
                pcoBits.append("1");
                pcoe1Bits.append("1");
                pcoe1e2Bits.append("1");
            } else {
                pBits.append("0");
                pcBits.append("0");
                pcoBits.append("0");
                pcoe1Bits.append("0");
                pcoe1e2Bits.append("0");
            }
            if (frame.isCopyrightBitSet()) {
                pcBits.append("1");
                pcoBits.append("1");
                pcoe1Bits.append("1");
                pcoe1e2Bits.append("1");
            } else {
                pcBits.append("0");
                pcoBits.append("0");
                pcoe1Bits.append("0");
                pcoe1e2Bits.append("0");
            }
            if (frame.isOriginalBitSet()) {
                pcoBits.append("1");
                pcoe1Bits.append("1");
                pcoe1e2Bits.append("1");
            } else {
                pcoBits.append("0");
                pcoe1Bits.append("0");
                pcoe1e2Bits.append("0");
            }
            if (frame.isEmphasisBit1Set()) {
                pcoe1Bits.append("1");
                pcoe1e2Bits.append("1");
            } else {
                pcoe1Bits.append("0");
                pcoe1e2Bits.append("0");
            }
            if (frame.isEmphasisBit2Set()) {
                pcoe1e2Bits.append("1");
                continue;
            }
            pcoe1e2Bits.append("0");
        }
        File outputFolder = new File("Extracted_Information");
        if (!outputFolder.exists()) {
            outputFolder.mkdir();
        }
        if (!(outputFolder = new File("Extracted_Information\\" + filePath.getName().toString())).exists()) {
            outputFolder.mkdir();
        }
        if (!(outputFolder = new File("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits")).exists()) {
            outputFolder.mkdir();
        }
        if (!(outputFolder = new File("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit")).exists()) {
            outputFolder.mkdir();
        }
        if (!(outputFolder = new File("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Byte")).exists()) {
            outputFolder.mkdir();
        }
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt"));
            bw.write(pBits.toString());
            bw.close();
            bw = new BufferedWriter(new FileWriter("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt"));
            bw.write(pcBits.toString());
            bw.close();
            bw = new BufferedWriter(new FileWriter("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt"));
            bw.write(pcoBits.toString());
            bw.close();
            bw = new BufferedWriter(new FileWriter("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt"));
            bw.write(pcoe1Bits.toString());
            bw.close();
            bw = new BufferedWriter(new FileWriter("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt"));
            bw.write(pcoe1e2Bits.toString());
            bw.close();
        }
        catch (IOException ex) {
            Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
        }
        ByteWriter.writeFrameHeaderBytesToFile(filePath, frameArrayList.size());
        int numStegazaurusLocationBits = 0;
        int covertDataBeginIndex = 0;
        String fileSignatureString = "";
        for (a = 0; a <= 4; ++a) {
            numStegazaurusLocationBits = (int)(Math.log(frameArrayList.size() * 1) / Math.log(2.0));
            if (Math.log(frameArrayList.size() * 1) / Math.log(2.0) - (double)numStegazaurusLocationBits > 0.0) {
                ++numStegazaurusLocationBits;
            }
            covertDataBeginIndex = a + numStegazaurusLocationBits;
            try {
                fileSignatureString = new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32);
                if (fileSignatureString.equals("11111111110110001111111111100000")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_JPEG_Image.jpg", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** JPEG file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.equals("10001001010100000100111001000111")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_PNG_Image.png", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** PNG file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 24).equals("010001110100100101000110")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_GIF_Image.gif", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** GIF file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 16).equals("0100001001001101")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_BMP_Image.bmp", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** BMP file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 24).equals("010010010100100100101010")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_TIFF_Image.tif", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** TIFF file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 16).equals("1111111111111010")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** MP3 file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 16).equals("1111111111111011")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** MP3 file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 24).equals("010010010100010000110011")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** MP3 file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 16).equals("0101000001001011")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_ZIP_Archive.zip", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** ZIP file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.equals("01010010011000010111001000100001")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_RAR_Archive.rar", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** RAR file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.equals("00100101010100000100010001000110")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_PDF_Document.pdf", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** PDF file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (!fileSignatureString.substring(0, 24).equals("010000010100010101010011")) continue;
                numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_AES_Encrypted_File.aes", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\Priv.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                foundFileString = "*** AES file found and extracted from Frame Header bits! ***";
                continue;
            }
            catch (FileNotFoundException ex) {
                Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }
            catch (IOException ex) {
                Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (a = 0; a <= 8; a += 2) {
            numStegazaurusLocationBits = (int)(Math.log(frameArrayList.size() * 2) / Math.log(2.0));
            if (Math.log(frameArrayList.size() * 2) / Math.log(2.0) - (double)numStegazaurusLocationBits > 0.0) {
                ++numStegazaurusLocationBits;
            }
            covertDataBeginIndex = a + numStegazaurusLocationBits;
            try {
                fileSignatureString = new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32);
                if (fileSignatureString.equals("11111111110110001111111111100000")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_JPEG_Image.jpg", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** JPEG file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.equals("10001001010100000100111001000111")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_PNG_Image.png", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** PNG file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 24).equals("010001110100100101000110")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_GIF_Image.gif", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** GIF file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 16).equals("0100001001001101")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_BMP_Image.bmp", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** BMP file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 24).equals("010010010100100100101010")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_TIFF_Image.tif", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** TIFF file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 16).equals("1111111111111010")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** MP3 file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 16).equals("1111111111111011")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** MP3 file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 24).equals("010010010100010000110011")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** MP3 file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 16).equals("0101000001001011")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_ZIP_Archive.zip", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** ZIP file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.equals("01010010011000010111001000100001")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_RAR_Archive.rar", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** RAR file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.equals("00100101010100000100010001000110")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_PDF_Document.pdf", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** PDF file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (!fileSignatureString.substring(0, 24).equals("010000010100010101010011")) continue;
                numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_AES_Encrypted_File.aes", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopy.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                foundFileString = "*** AES file found and extracted from Frame Header bits! ***";
                continue;
            }
            catch (FileNotFoundException ex) {
                Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }
            catch (IOException ex) {
                Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (a = 0; a <= 12; a += 3) {
            numStegazaurusLocationBits = (int)(Math.log(frameArrayList.size() * 3) / Math.log(2.0));
            if (Math.log(frameArrayList.size() * 3) / Math.log(2.0) - (double)numStegazaurusLocationBits > 0.0) {
                ++numStegazaurusLocationBits;
            }
            covertDataBeginIndex = a + numStegazaurusLocationBits;
            try {
                fileSignatureString = new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32);
                if (fileSignatureString.equals("11111111110110001111111111100000")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_JPEG_Image.jpg", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** JPEG file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.equals("10001001010100000100111001000111")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_PNG_Image.png", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** PNG file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 24).equals("010001110100100101000110")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_GIF_Image.gif", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** GIF file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 16).equals("0100001001001101")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_BMP_Image.bmp", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** BMP file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 24).equals("010010010100100100101010")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_TIFF_Image.tif", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** TIFF file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 16).equals("1111111111111010")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** MP3 file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 16).equals("1111111111111011")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** MP3 file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 24).equals("010010010100010000110011")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** MP3 file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 16).equals("0101000001001011")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_ZIP_Archive.zip", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** ZIP file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.equals("01010010011000010111001000100001")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_RAR_Archive.rar", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** RAR file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.equals("00100101010100000100010001000110")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_PDF_Document.pdf", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** PDF file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (!fileSignatureString.substring(0, 24).equals("010000010100010101010011")) continue;
                numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_AES_Encrypted_File.aes", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrig.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                foundFileString = "*** AES file found and extracted from Frame Header bits! ***";
                continue;
            }
            catch (FileNotFoundException ex) {
                Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }
            catch (IOException ex) {
                Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (a = 0; a <= 16; a += 4) {
            numStegazaurusLocationBits = (int)(Math.log(frameArrayList.size() * 4) / Math.log(2.0));
            if (Math.log(frameArrayList.size() * 4) / Math.log(2.0) - (double)numStegazaurusLocationBits > 0.0) {
                ++numStegazaurusLocationBits;
            }
            covertDataBeginIndex = a + numStegazaurusLocationBits;
            try {
                fileSignatureString = new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32);
                if (fileSignatureString.equals("11111111110110001111111111100000")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_JPEG_Image.jpg", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** JPEG file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.equals("10001001010100000100111001000111")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_PNG_Image.png", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** PNG file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 24).equals("010001110100100101000110")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_GIF_Image.gif", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** GIF file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 16).equals("0100001001001101")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_BMP_Image.bmp", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** BMP file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 24).equals("010010010100100100101010")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_TIFF_Image.tif", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** TIFF file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 16).equals("1111111111111010")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** MP3 file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 16).equals("1111111111111011")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** MP3 file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 24).equals("010010010100010000110011")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** MP3 file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 16).equals("0101000001001011")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_ZIP_Archive.zip", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** ZIP file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.equals("01010010011000010111001000100001")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_RAR_Archive.rar", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** RAR file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.equals("00100101010100000100010001000110")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_PDF_Document.pdf", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** PDF file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (!fileSignatureString.substring(0, 24).equals("010000010100010101010011")) continue;
                numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_AES_Encrypted_File.aes", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                foundFileString = "*** AES file found and extracted from Frame Header bits! ***";
                continue;
            }
            catch (FileNotFoundException ex) {
                Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }
            catch (IOException ex) {
                Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        for (a = 0; a <= 20; a += 5) {
            numStegazaurusLocationBits = (int)(Math.log(frameArrayList.size() * 5) / Math.log(2.0));
            if (Math.log(frameArrayList.size() * 5) / Math.log(2.0) - (double)numStegazaurusLocationBits > 0.0) {
                ++numStegazaurusLocationBits;
            }
            covertDataBeginIndex = a + numStegazaurusLocationBits;
            try {
                fileSignatureString = new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32);
                if (fileSignatureString.equals("11111111110110001111111111100000")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_JPEG_Image.jpg", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** JPEG file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.equals("10001001010100000100111001000111")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_PNG_Image.png", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** PNG file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 24).equals("010001110100100101000110")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_GIF_Image.gif", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** GIF file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 16).equals("0100001001001101")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_BMP_Image.bmp", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** BMP file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 24).equals("010010010100100100101010")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_TIFF_Image.tif", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** TIFF file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 16).equals("1111111111111010")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** MP3 file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 16).equals("1111111111111011")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** MP3 file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 24).equals("010010010100010000110011")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** MP3 file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.substring(0, 16).equals("0101000001001011")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_ZIP_Archive.zip", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** ZIP file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.equals("01010010011000010111001000100001")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_RAR_Archive.rar", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** RAR file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (fileSignatureString.equals("00100101010100000100010001000110")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_PDF_Document.pdf", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** PDF file found and extracted from Frame Header bits! ***";
                    continue;
                }
                if (!fileSignatureString.substring(0, 24).equals("010000010100010101010011")) continue;
                numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(a, a + numStegazaurusLocationBits));
                ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Extracted_AES_Encrypted_File.aes", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\FrameHeaderBits\\Bit\\PrivCopyOrigEmph1Emph2.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                foundFileString = "*** AES file found and extracted from Frame Header bits! ***";
                continue;
            }
            catch (FileNotFoundException ex) {
                Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }
            catch (IOException ex) {
                Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        totalBitStateChanges = privateBitStateChangeCounter + copyrightBitStateChangeCounter + originalBitStateChangeCounter + emphasisBit1StateChangeCounter + emphasisBit2StateChangeCounter;
        System.out.println("\n\n\n--------------------- Frame Header Bit Scan Findings ---------------------");
        System.out.println("\nNumber of suspect Frame Header bits in file: " + frameArrayList.size() * 5);
        System.out.println("Number of Private Bit anomalies detected:    " + privateBitStateChangeCounter);
        System.out.println("Number of Copyright Bit anomalies detected:  " + copyrightBitStateChangeCounter);
        System.out.println("Number of Original Bit anomalies detected:   " + originalBitStateChangeCounter);
        System.out.println("Number of Emphasis Bit#1 anomalies detected: " + emphasisBit1StateChangeCounter);
        System.out.println("Number of Emphasis Bit#2 anomalies detected: " + emphasisBit2StateChangeCounter);
        System.out.println("Total number of anomalies detected:          " + totalBitStateChanges);
        System.out.print("\nAnomalous Frame Header bit percentage:       ");
        System.out.printf("%1$.2f", (double)totalBitStateChanges / (double)(frameArrayList.size() * 5) * 100.0);
        System.out.println("%");
        System.out.println("\nFiles now available in the Extracted_Information directory");
        System.out.println("representing all extracted information in bit and byte formats.");
        System.out.println("\n" + foundFileString);
    }

    private static void scanSideInformationBitsCMD(ArrayList<Frame> frameArrayList, File filePath) {
        int numSideInformationPaddingBits = 0;
        int numSideInformationPaddingBitAnomalies = 0;
        String foundFileString = "No files were found within the extracted Side Information data.";
        File outputFolder = new File("Extracted_Information");
        if (!outputFolder.exists()) {
            outputFolder.mkdir();
        }
        if (!(outputFolder = new File("Extracted_Information\\" + filePath.getName().toString())).exists()) {
            outputFolder.mkdir();
        }
        if (!(outputFolder = new File("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits")).exists()) {
            outputFolder.mkdir();
        }
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt"));
        }
        catch (IOException ex) {
            Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Frame frame : frameArrayList) {
            if (frame.getChannelMode().equals("Single Channel")) {
                try {
                    if ((frame.getSideInformationBytes().get(1).byteValue() & 4) != 0) {
                        ++numSideInformationPaddingBitAnomalies;
                        bw.write(49);
                    } else {
                        bw.write(48);
                    }
                    if ((frame.getSideInformationBytes().get(1).byteValue() & 8) != 0) {
                        ++numSideInformationPaddingBitAnomalies;
                        bw.write(49);
                    } else {
                        bw.write(48);
                    }
                    if ((frame.getSideInformationBytes().get(1).byteValue() & 16) != 0) {
                        ++numSideInformationPaddingBitAnomalies;
                        bw.write(49);
                    } else {
                        bw.write(48);
                    }
                    if ((frame.getSideInformationBytes().get(1).byteValue() & 32) != 0) {
                        ++numSideInformationPaddingBitAnomalies;
                        bw.write(49);
                    } else {
                        bw.write(48);
                    }
                    if ((frame.getSideInformationBytes().get(1).byteValue() & 64) != 0) {
                        ++numSideInformationPaddingBitAnomalies;
                        bw.write(49);
                    } else {
                        bw.write(48);
                    }
                    numSideInformationPaddingBits += 5;
                }
                catch (IOException ex) {
                    Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
                }
                continue;
            }
            try {
                if ((frame.getSideInformationBytes().get(1).byteValue() & 16) != 0) {
                    ++numSideInformationPaddingBitAnomalies;
                    bw.write(49);
                } else {
                    bw.write(48);
                }
                if ((frame.getSideInformationBytes().get(1).byteValue() & 32) != 0) {
                    ++numSideInformationPaddingBitAnomalies;
                    bw.write(49);
                } else {
                    bw.write(48);
                }
                if ((frame.getSideInformationBytes().get(1).byteValue() & 64) != 0) {
                    ++numSideInformationPaddingBitAnomalies;
                    bw.write(49);
                } else {
                    bw.write(48);
                }
                numSideInformationPaddingBits += 3;
            }
            catch (IOException ex) {
                Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            bw.close();
        }
        catch (IOException ex) {
            Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
        }
        ByteWriter.writeBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits", numSideInformationPaddingBits, 0, "SI");
        int numStegazaurusLocationBits = 0;
        int covertDataBeginIndex = 0;
        numStegazaurusLocationBits = (int)(Math.log(numSideInformationPaddingBits) / Math.log(2.0));
        if (Math.log(numSideInformationPaddingBits) / Math.log(2.0) - (double)numStegazaurusLocationBits > 0.0) {
            ++numStegazaurusLocationBits;
        }
        covertDataBeginIndex = numStegazaurusLocationBits;
        try {
            if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32).equals("11111111110110001111111111100000")) {
                int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(0, numStegazaurusLocationBits));
                ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Extracted_JPEG_Image.jpg", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                foundFileString = "*** JPEG file found and extracted from Side Information Padding Bits! ***";
            } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32).equals("10001001010100000100111001000111")) {
                int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(0, numStegazaurusLocationBits));
                ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Extracted_PNG_Image.png", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                foundFileString = "*** PNG file found and extracted from Side Information Padding Bits! ***";
            } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 24).equals("010001110100100101000110")) {
                int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(0, numStegazaurusLocationBits));
                ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Extracted_GIF_Image.gif", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                foundFileString = "*** GIF file found and extracted from Side Information Padding Bits! ***";
            } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 16).equals("0100001001001101")) {
                int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(0, numStegazaurusLocationBits));
                ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Extracted_BMP_Image.bmp", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                foundFileString = "*** BMP file found and extracted from Side Information Padding Bits! ***";
            } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 24).equals("010010010100100100101010")) {
                int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(0, numStegazaurusLocationBits));
                ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Extracted_TIFF_Image.tif", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                foundFileString = "*** TIFF file found and extracted from Side Information Padding Bits! ***";
            } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 16).equals("1111111111111010")) {
                int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(0, numStegazaurusLocationBits));
                ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                foundFileString = "*** MP3 file found and extracted from Side Information Padding Bits! ***";
            } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 16).equals("1111111111111011")) {
                int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(0, numStegazaurusLocationBits));
                ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                foundFileString = "*** MP3 file found and extracted from Side Information Padding Bits! ***";
            } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 24).equals("010010010100010000110011")) {
                int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(0, numStegazaurusLocationBits));
                ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                foundFileString = "*** MP3 file found and extracted from Side Information Padding Bits! ***";
            } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 16).equals("0101000001001011")) {
                int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(0, numStegazaurusLocationBits));
                ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Extracted_ZIP_Archive.zip", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                foundFileString = "*** ZIP file found and extracted from Side Information Padding Bits! ***";
            } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32).equals("01010010011000010111001000100001")) {
                int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(0, numStegazaurusLocationBits));
                ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Extracted_RAR_Archive.rar", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                foundFileString = "*** RAR file found and extracted from Side Information Padding Bits! ***";
            } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32).equals("00100101010100000100010001000110")) {
                int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(0, numStegazaurusLocationBits));
                ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Extracted_PDF_Document.pdf", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                foundFileString = "*** PDF file found and extracted from Side Information Padding Bits! ***";
            } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 24).equals("010000010100010101010011")) {
                int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(0, numStegazaurusLocationBits));
                ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Extracted_AES_Encrypted_File.aes", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\SideInformationPaddingBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                foundFileString = "*** AES file found and extracted from Side Information Padding Bits! ***";
            }
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("\n\n\n--------------- Side Information Padding Bit Scan Findings ---------------");
        System.out.println("\nNumber of suspect Side Information Padding bits in file:    " + numSideInformationPaddingBits);
        System.out.println("Number of anomalous Side Information Padding bits detected: " + numSideInformationPaddingBitAnomalies);
        System.out.print("\nAnomalous Side Information Padding bit percentage:          ");
        System.out.printf("%1$.2f", (double)numSideInformationPaddingBitAnomalies / (double)numSideInformationPaddingBits * 100.0);
        System.out.println("%");
        System.out.println("\nFiles now available in the Extracted_Information directory");
        System.out.println("representing all extracted information in bit and byte formats.");
        System.out.println("\n" + foundFileString);
    }

    private static void scanEmptyFrameBitsWithoutAncillaryCMD(ArrayList<Frame> frameArrayList, File filePath) {
        int totalPart2_3_lengthBits = 0;
        int numEmptyFrameBitsInLastByte = 0;
        int firstByteIndex = 0;
        int lastByteIndex = 0;
        int totalNumEmptyFrameBits = 0;
        boolean skipBitReservoirAdd = false;
        ArrayList<Byte> bitReservoir = new ArrayList<Byte>();
        int emptyFrameIndex = 0;
        int numEmptyFrameBitsInFirstFrame = 0;
        String foundFileString = "No files were found within the extracted Empty Frame information.";
        File outputFolder = new File("Extracted_Information");
        if (!outputFolder.exists()) {
            outputFolder.mkdir();
        }
        if (!(outputFolder = new File("Extracted_Information\\" + filePath.getName().toString())).exists()) {
            outputFolder.mkdir();
        }
        if (!(outputFolder = new File("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary")).exists()) {
            outputFolder.mkdir();
        }
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt"));
        }
        catch (IOException ex) {
            Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Frame frame : frameArrayList) {
            if (frame.getBig_valuesLengthBits1() + frame.getBig_valuesLengthBits2() + frame.getBig_valuesLengthBits3() + frame.getBig_valuesLengthBits4() + frame.getTable_selectBits1() + frame.getTable_selectBits2() + frame.getTable_selectBits3() + frame.getTable_selectBits4() == 0) {
                int a;
                totalPart2_3_lengthBits = frame.getPart2_3_lengthBits1() + frame.getPart2_3_lengthBits2() + frame.getPart2_3_lengthBits3() + frame.getPart2_3_lengthBits4();
                numEmptyFrameBitsInLastByte = totalPart2_3_lengthBits % 8;
                if (frame.getMain_data_beginBytes() == 0) {
                    try {
                        lastByteIndex = totalPart2_3_lengthBits / 8;
                        if (numEmptyFrameBitsInLastByte == 0) {
                            --lastByteIndex;
                        }
                        for (a = 0; a < lastByteIndex; ++a) {
                            if ((frame.getMainDataBytes().get(a).byteValue() & 128) != 0) {
                                bw.write(49);
                            } else {
                                bw.write(48);
                            }
                            if ((frame.getMainDataBytes().get(a).byteValue() & 64) != 0) {
                                bw.write(49);
                            } else {
                                bw.write(48);
                            }
                            if ((frame.getMainDataBytes().get(a).byteValue() & 32) != 0) {
                                bw.write(49);
                            } else {
                                bw.write(48);
                            }
                            if ((frame.getMainDataBytes().get(a).byteValue() & 16) != 0) {
                                bw.write(49);
                            } else {
                                bw.write(48);
                            }
                            if ((frame.getMainDataBytes().get(a).byteValue() & 8) != 0) {
                                bw.write(49);
                            } else {
                                bw.write(48);
                            }
                            if ((frame.getMainDataBytes().get(a).byteValue() & 4) != 0) {
                                bw.write(49);
                            } else {
                                bw.write(48);
                            }
                            if ((frame.getMainDataBytes().get(a).byteValue() & 2) != 0) {
                                bw.write(49);
                            } else {
                                bw.write(48);
                            }
                            if ((frame.getMainDataBytes().get(a).byteValue() & 1) != 0) {
                                bw.write(49);
                            } else {
                                bw.write(48);
                            }
                            totalNumEmptyFrameBits += 8;
                            if (emptyFrameIndex != 0) continue;
                            numEmptyFrameBitsInFirstFrame += 8;
                        }
                        for (a = 0; a < numEmptyFrameBitsInLastByte; ++a) {
                            bw.write(BytesToBinary.getBinaryString(frame.getMainDataBytes().get(lastByteIndex).byteValue()).charAt(a));
                            ++totalNumEmptyFrameBits;
                            if (emptyFrameIndex != 0 && emptyFrameIndex != 1) continue;
                            ++numEmptyFrameBitsInFirstFrame;
                        }
                    }
                    catch (IOException ex) {
                        Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (frame.getMain_data_beginBytes() != 0) {
                    skipBitReservoirAdd = true;
                    bitReservoir.addAll(frame.getMainDataBytes());
                    try {
                        firstByteIndex = bitReservoir.size() - frame.getMainDataBytes().size() - frame.getMain_data_beginBytes();
                        lastByteIndex = firstByteIndex + totalPart2_3_lengthBits / 8;
                        if (numEmptyFrameBitsInLastByte == 0) {
                            --lastByteIndex;
                        }
                        for (a = firstByteIndex; a < lastByteIndex; ++a) {
                            if ((((Byte)bitReservoir.get(a)).byteValue() & 128) != 0) {
                                bw.write(49);
                            } else {
                                bw.write(48);
                            }
                            if ((((Byte)bitReservoir.get(a)).byteValue() & 64) != 0) {
                                bw.write(49);
                            } else {
                                bw.write(48);
                            }
                            if ((((Byte)bitReservoir.get(a)).byteValue() & 32) != 0) {
                                bw.write(49);
                            } else {
                                bw.write(48);
                            }
                            if ((((Byte)bitReservoir.get(a)).byteValue() & 16) != 0) {
                                bw.write(49);
                            } else {
                                bw.write(48);
                            }
                            if ((((Byte)bitReservoir.get(a)).byteValue() & 8) != 0) {
                                bw.write(49);
                            } else {
                                bw.write(48);
                            }
                            if ((((Byte)bitReservoir.get(a)).byteValue() & 4) != 0) {
                                bw.write(49);
                            } else {
                                bw.write(48);
                            }
                            if ((((Byte)bitReservoir.get(a)).byteValue() & 2) != 0) {
                                bw.write(49);
                            } else {
                                bw.write(48);
                            }
                            if ((((Byte)bitReservoir.get(a)).byteValue() & 1) != 0) {
                                bw.write(49);
                            } else {
                                bw.write(48);
                            }
                            totalNumEmptyFrameBits += 8;
                            if (emptyFrameIndex != 0) continue;
                            numEmptyFrameBitsInFirstFrame += 8;
                        }
                        for (a = 0; a < numEmptyFrameBitsInLastByte; ++a) {
                            bw.write(BytesToBinary.getBinaryString(((Byte)bitReservoir.get(lastByteIndex)).byteValue()).charAt(a));
                            ++totalNumEmptyFrameBits;
                            if (emptyFrameIndex != 0 && emptyFrameIndex != 1) continue;
                            ++numEmptyFrameBitsInFirstFrame;
                        }
                    }
                    catch (IOException ex) {
                        Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                ++emptyFrameIndex;
            }
            if (skipBitReservoirAdd) continue;
            bitReservoir.addAll(frame.getMainDataBytes());
        }
        try {
            bw.close();
        }
        catch (IOException ex) {
            Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
        }
        ByteWriter.writeBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary", totalNumEmptyFrameBits, numEmptyFrameBitsInFirstFrame, "EFWA");
        if (totalNumEmptyFrameBits > 0) {
            int numStegazaurusLocationBits = 0;
            int covertDataBeginIndex = 0;
            numStegazaurusLocationBits = (int)(Math.log(totalNumEmptyFrameBits) / Math.log(2.0));
            if (Math.log(totalNumEmptyFrameBits) / Math.log(2.0) - (double)numStegazaurusLocationBits > 0.0) {
                ++numStegazaurusLocationBits;
            }
            if (totalNumEmptyFrameBits >= (covertDataBeginIndex = numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits) + 32) {
                try {
                    int numberOfBitsToBeWritten;
                    if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32).equals("11111111110110001111111111100000")) {
                        numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Extracted_JPEG_Image.jpg", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** JPEG file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32).equals("10001001010100000100111001000111")) {
                        numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Extracted_PNG_Image.png", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** PNG file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 24).equals("010001110100100101000110")) {
                        numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Extracted_GIF_Image.gif", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** GIF file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 16).equals("0100001001001101")) {
                        numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Extracted_BMP_Image.bmp", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** BMP file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 24).equals("010010010100100100101010")) {
                        numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Extracted_TIFF_Image.tif", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** TIFF file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 16).equals("1111111111111010")) {
                        numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** MP3 file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 16).equals("1111111111111011")) {
                        numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** MP3 file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 24).equals("010010010100010000110011")) {
                        numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** MP3 file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 16).equals("0101000001001011")) {
                        numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Extracted_ZIP_Archive.zip", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** ZIP file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32).equals("01010010011000010111001000100001")) {
                        numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Extracted_RAR_Archive.rar", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** RAR file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32).equals("00100101010100000100010001000110")) {
                        numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Extracted_PDF_Document.pdf", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** PDF file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 24).equals("010000010100010101010011")) {
                        numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Extracted_AES_Encrypted_File.aes", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithoutAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** AES file found and extracted from Empty Frame Bits! ***";
                    }
                }
                catch (FileNotFoundException ex) {
                    Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch (IOException ex) {
                    Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("\n\n\n--------------------- Empty Frame Bit Scan Findings ---------------------");
        if (totalNumEmptyFrameBits != 0) {
            System.out.println("\n" + totalNumEmptyFrameBits + " Empty Frame Bits were found and extracted.\n");
            System.out.println("Please view files in the Extracted_Information\\EmptyFrameBitsWithoutAncillary");
            System.out.println("directory for bit and byte representations of this extracted information.");
        } else {
            System.out.println("\nThis file contains 0 Empty Frame Bits.");
        }
        System.out.println("\n" + foundFileString);
    }

    private static void scanEmptyFrameBitsWithAncillaryCMD(ArrayList<Frame> frameArrayList, File filePath) {
        int numEmptyFrameBits = 0;
        ArrayList<Byte> bitReservoir = new ArrayList<Byte>();
        int emptyFrameIndex = 0;
        int numEmptyFrameBitsInFirstFrame = 0;
        String foundFileString = "No files were found within the extracted Empty Frame information.";
        File outputFolder = new File("Extracted_Information");
        if (!outputFolder.exists()) {
            outputFolder.mkdir();
        }
        if (!(outputFolder = new File("Extracted_Information\\" + filePath.getName().toString())).exists()) {
            outputFolder.mkdir();
        }
        if (!(outputFolder = new File("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary")).exists()) {
            outputFolder.mkdir();
        }
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt"));
        }
        catch (IOException ex) {
            Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Frame frame : frameArrayList) {
            if (frame.getBig_valuesLengthBits1() + frame.getBig_valuesLengthBits2() + frame.getBig_valuesLengthBits3() + frame.getBig_valuesLengthBits4() + frame.getTable_selectBits1() + frame.getTable_selectBits2() + frame.getTable_selectBits3() + frame.getTable_selectBits4() == 0) {
                if (frame.getMain_data_beginBytes() == 0) {
                    if (frame.getId() == frameArrayList.size() - 1 || frameArrayList.get(frame.getId() + 1).getMain_data_beginBytes() == 0) {
                        try {
                            for (Byte b : frame.getMainDataBytes()) {
                                if ((b.byteValue() & 128) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((b.byteValue() & 64) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((b.byteValue() & 32) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((b.byteValue() & 16) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((b.byteValue() & 8) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((b.byteValue() & 4) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((b.byteValue() & 2) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((b.byteValue() & 1) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                numEmptyFrameBits += 8;
                                if (emptyFrameIndex != 0) continue;
                                numEmptyFrameBitsInFirstFrame += 8;
                            }
                        }
                        catch (IOException ex) {
                            Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        try {
                            for (int a = 0; a < frame.getMainDataBytes().size() - frameArrayList.get(frame.getId() + 1).getMain_data_beginBytes(); ++a) {
                                if ((frame.getMainDataBytes().get(a).byteValue() & 128) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((frame.getMainDataBytes().get(a).byteValue() & 64) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((frame.getMainDataBytes().get(a).byteValue() & 32) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((frame.getMainDataBytes().get(a).byteValue() & 16) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((frame.getMainDataBytes().get(a).byteValue() & 8) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((frame.getMainDataBytes().get(a).byteValue() & 4) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((frame.getMainDataBytes().get(a).byteValue() & 2) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((frame.getMainDataBytes().get(a).byteValue() & 1) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                numEmptyFrameBits += 8;
                                if (emptyFrameIndex != 0) continue;
                                numEmptyFrameBitsInFirstFrame += 8;
                            }
                        }
                        catch (IOException ex) {
                            Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else if (frame.getMain_data_beginBytes() > 0) {
                    if (frame.getId() == frameArrayList.size() - 1 || frameArrayList.get(frame.getId() + 1).getMain_data_beginBytes() == 0) {
                        try {
                            for (int a = bitReservoir.size() - frame.getMain_data_beginBytes(); a < bitReservoir.size(); ++a) {
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 128) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 64) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 32) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 16) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 8) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 4) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 2) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 1) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                numEmptyFrameBits += 8;
                                if (emptyFrameIndex != 0) continue;
                                numEmptyFrameBitsInFirstFrame += 8;
                            }
                            for (Byte b : frame.getMainDataBytes()) {
                                if ((b.byteValue() & 128) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((b.byteValue() & 64) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((b.byteValue() & 32) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((b.byteValue() & 16) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((b.byteValue() & 8) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((b.byteValue() & 4) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((b.byteValue() & 2) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((b.byteValue() & 1) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                numEmptyFrameBits += 8;
                                if (emptyFrameIndex != 0) continue;
                                numEmptyFrameBitsInFirstFrame += 8;
                            }
                        }
                        catch (IOException ex) {
                            Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (frameArrayList.get(frame.getId() + 1).getMain_data_beginBytes() <= frame.getMainDataBytes().size()) {
                        try {
                            int a;
                            for (a = bitReservoir.size() - frame.getMain_data_beginBytes(); a < bitReservoir.size(); ++a) {
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 128) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 64) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 32) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 16) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 8) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 4) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 2) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 1) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                numEmptyFrameBits += 8;
                                if (emptyFrameIndex != 0) continue;
                                numEmptyFrameBitsInFirstFrame += 8;
                            }
                            for (a = 0; a < frame.getMainDataBytes().size() - frameArrayList.get(frame.getId() + 1).getMain_data_beginBytes(); ++a) {
                                if ((frame.getMainDataBytes().get(a).byteValue() & 128) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((frame.getMainDataBytes().get(a).byteValue() & 64) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((frame.getMainDataBytes().get(a).byteValue() & 32) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((frame.getMainDataBytes().get(a).byteValue() & 16) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((frame.getMainDataBytes().get(a).byteValue() & 8) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((frame.getMainDataBytes().get(a).byteValue() & 4) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((frame.getMainDataBytes().get(a).byteValue() & 2) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((frame.getMainDataBytes().get(a).byteValue() & 1) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                numEmptyFrameBits += 8;
                                if (emptyFrameIndex != 0) continue;
                                numEmptyFrameBitsInFirstFrame += 8;
                            }
                        }
                        catch (IOException ex) {
                            Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (frameArrayList.get(frame.getId() + 1).getMain_data_beginBytes() > frame.getMainDataBytes().size()) {
                        try {
                            for (int a = bitReservoir.size() - frame.getMain_data_beginBytes(); a < bitReservoir.size() - (frameArrayList.get(frame.getId() + 1).getMain_data_beginBytes() - frame.getMainDataBytes().size()); ++a) {
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 128) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 64) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 32) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 16) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 8) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 4) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 2) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                if ((((Byte)bitReservoir.get(a)).byteValue() & 1) != 0) {
                                    bw.write(49);
                                } else {
                                    bw.write(48);
                                }
                                numEmptyFrameBits += 8;
                                if (emptyFrameIndex != 0) continue;
                                numEmptyFrameBitsInFirstFrame += 8;
                            }
                        }
                        catch (IOException ex) {
                            Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                ++emptyFrameIndex;
            }
            bitReservoir.addAll(frame.getMainDataBytes());
        }
        try {
            bw.close();
        }
        catch (IOException ex) {
            Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
        }
        ByteWriter.writeBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary", numEmptyFrameBits, numEmptyFrameBitsInFirstFrame, "EFWA");
        if (numEmptyFrameBits > 0) {
            int numStegazaurusLocationBits = 0;
            int covertDataBeginIndex = 0;
            numStegazaurusLocationBits = (int)(Math.log(numEmptyFrameBits) / Math.log(2.0));
            if (Math.log(numEmptyFrameBits) / Math.log(2.0) - (double)numStegazaurusLocationBits > 0.0) {
                ++numStegazaurusLocationBits;
            }
            if (numEmptyFrameBits >= (covertDataBeginIndex = numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits) + 32) {
                try {
                    if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32).equals("11111111110110001111111111100000")) {
                        int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Extracted_JPEG_Image.jpg", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** JPEG file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32).equals("10001001010100000100111001000111")) {
                        int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Extracted_PNG_Image.png", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** PNG file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 24).equals("010001110100100101000110")) {
                        int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Extracted_GIF_Image.gif", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** GIF file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 16).equals("0100001001001101")) {
                        int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Extracted_BMP_Image.bmp", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** BMP file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 24).equals("010010010100100100101010")) {
                        int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Extracted_TIFF_Image.tif", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** TIFF file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 16).equals("1111111111111010")) {
                        int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** MP3 file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 16).equals("1111111111111011")) {
                        int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** MP3 file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 24).equals("010010010100010000110011")) {
                        int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** MP3 file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 16).equals("0101000001001011")) {
                        int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Extracted_ZIP_Archive.zip", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** ZIP file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32).equals("01010010011000010111001000100001")) {
                        int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Extracted_RAR_Archive.rar", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** RAR file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32).equals("00100101010100000100010001000110")) {
                        int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Extracted_PDF_Document.pdf", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** PDF file found and extracted from Empty Frame Bits! ***";
                    } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 24).equals("010000010100010101010011")) {
                        int numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(numEmptyFrameBitsInFirstFrame, numEmptyFrameBitsInFirstFrame + numStegazaurusLocationBits));
                        ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Extracted_AES_Encrypted_File.aes", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\EmptyFrameBitsWithAncillary\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                        foundFileString = "*** AES file found and extracted from Empty Frame Bits! ***";
                    }
                }
                catch (FileNotFoundException ex) {
                    Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch (IOException ex) {
                    Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("\n\n\n--------------- Empty Frame Bit + Ancillary Scan Findings ---------------");
        if (numEmptyFrameBits != 0) {
            System.out.println("\n" + numEmptyFrameBits + " Empty Frame + Ancillary Bits were found and extracted.\n");
            System.out.println("Please view files in the Extracted_Information\\EmptyFrameBitsWithAncillary");
            System.out.println("directory for bit and byte representations of this extracted information.");
        } else {
            System.out.println("\nThis file contains 0 Empty Frame + Ancillary Bits.");
        }
        System.out.println("\n" + foundFileString);
    }

    private static void scanAncillaryBitsCMD(ArrayList<Frame> frameArrayList, File filePath) {
        int totalPart2_3_lengthBits = 0;
        int numAncillaryBitsInLastByte = 0;
        int lastByteIndex = 0;
        int totalNumAncillaryBits = 0;
        int nextMainDataBeginIndex = 0;
        boolean skipBitReservoirAdd = false;
        int bitCheckIndex = 0;
        char evenBitCheckIndexValue = ' ';
        char oddBitCheckIndexValue = ' ';
        int numAncillaryBitAnomalies = 0;
        ArrayList<Byte> bitReservoir = new ArrayList<Byte>();
        int ancillaryFrameIndex = 0;
        int numAncillaryBitsInFirst2Frames = 0;
        String foundFileString = "No files were found within the extracted Ancillary information.";
        File outputFolder = new File("Extracted_Information");
        if (!outputFolder.exists()) {
            outputFolder.mkdir();
        }
        if (!(outputFolder = new File("Extracted_Information\\" + filePath.getName().toString())).exists()) {
            outputFolder.mkdir();
        }
        if (!(outputFolder = new File("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits")).exists()) {
            outputFolder.mkdir();
        }
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt"));
        }
        catch (IOException ex) {
            Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Frame frame : frameArrayList) {
            if (frame.getBig_valuesLengthBits1() + frame.getBig_valuesLengthBits2() + frame.getBig_valuesLengthBits3() + frame.getBig_valuesLengthBits4() + frame.getTable_selectBits1() + frame.getTable_selectBits2() + frame.getTable_selectBits3() + frame.getTable_selectBits4() != 0) {
                int b;
                int a;
                totalPart2_3_lengthBits = frame.getPart2_3_lengthBits1() + frame.getPart2_3_lengthBits2() + frame.getPart2_3_lengthBits3() + frame.getPart2_3_lengthBits4();
                numAncillaryBitsInLastByte = 8 - totalPart2_3_lengthBits % 8;
                if (numAncillaryBitsInLastByte == 8) {
                    numAncillaryBitsInLastByte = 0;
                }
                skipBitReservoirAdd = false;
                if (frame.getMain_data_beginBytes() == 0 && frame.getId() != frameArrayList.size() - 1) {
                    try {
                        lastByteIndex = totalPart2_3_lengthBits / 8;
                        if (numAncillaryBitsInLastByte == 0) {
                            --lastByteIndex;
                        }
                        nextMainDataBeginIndex = frame.getMainDataBytes().size() - frameArrayList.get(frame.getId() + 1).getMain_data_beginBytes();
                        for (a = 8 - numAncillaryBitsInLastByte; a < 8; ++a) {
                            bw.write(BytesToBinary.getBinaryString(frame.getMainDataBytes().get(lastByteIndex).byteValue()).charAt(a));
                            if (bitCheckIndex == 0) {
                                evenBitCheckIndexValue = BytesToBinary.getBinaryString(frame.getMainDataBytes().get(lastByteIndex).byteValue()).charAt(a);
                            }
                            if (bitCheckIndex == 1) {
                                oddBitCheckIndexValue = BytesToBinary.getBinaryString(frame.getMainDataBytes().get(lastByteIndex).byteValue()).charAt(a);
                            }
                            if (bitCheckIndex % 2 == 0 && BytesToBinary.getBinaryString(frame.getMainDataBytes().get(lastByteIndex).byteValue()).charAt(a) != evenBitCheckIndexValue) {
                                ++numAncillaryBitAnomalies;
                            }
                            if (bitCheckIndex % 2 != 0 && BytesToBinary.getBinaryString(frame.getMainDataBytes().get(lastByteIndex).byteValue()).charAt(a) != oddBitCheckIndexValue) {
                                ++numAncillaryBitAnomalies;
                            }
                            ++totalNumAncillaryBits;
                            ++bitCheckIndex;
                            if (ancillaryFrameIndex != 0 && ancillaryFrameIndex != 1) continue;
                            ++numAncillaryBitsInFirst2Frames;
                        }
                        for (a = lastByteIndex + 1; a < nextMainDataBeginIndex; ++a) {
                            for (b = 0; b < 8; ++b) {
                                bw.write(BytesToBinary.getBinaryString(frame.getMainDataBytes().get(a).byteValue()).charAt(b));
                                if (bitCheckIndex % 2 == 0 && BytesToBinary.getBinaryString(frame.getMainDataBytes().get(a).byteValue()).charAt(b) != evenBitCheckIndexValue) {
                                    ++numAncillaryBitAnomalies;
                                }
                                if (bitCheckIndex % 2 != 0 && BytesToBinary.getBinaryString(frame.getMainDataBytes().get(a).byteValue()).charAt(b) != oddBitCheckIndexValue) {
                                    ++numAncillaryBitAnomalies;
                                }
                                ++totalNumAncillaryBits;
                                ++bitCheckIndex;
                                if (ancillaryFrameIndex != 0 && ancillaryFrameIndex != 1) continue;
                                ++numAncillaryBitsInFirst2Frames;
                            }
                        }
                    }
                    catch (IOException ex) {
                        Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (frame.getMain_data_beginBytes() != 0 && frame.getId() != frameArrayList.size() - 1) {
                    if (frameArrayList.get(frame.getId() + 1).getMain_data_beginBytes() <= frame.getMainDataBytes().size()) {
                        skipBitReservoirAdd = true;
                        bitReservoir.addAll(frame.getMainDataBytes());
                        try {
                            lastByteIndex = totalPart2_3_lengthBits / 8;
                            if (numAncillaryBitsInLastByte == 0) {
                                --lastByteIndex;
                            }
                            nextMainDataBeginIndex = bitReservoir.size() - frameArrayList.get(frame.getId() + 1).getMain_data_beginBytes();
                            for (a = 8 - numAncillaryBitsInLastByte; a < 8; ++a) {
                                bw.write(BytesToBinary.getBinaryString(((Byte)bitReservoir.get(bitReservoir.size() - frame.getMainDataBytes().size() - frame.getMain_data_beginBytes() + lastByteIndex)).byteValue()).charAt(a));
                                if (bitCheckIndex == 0) {
                                    evenBitCheckIndexValue = BytesToBinary.getBinaryString(((Byte)bitReservoir.get(bitReservoir.size() - frame.getMainDataBytes().size() - frame.getMain_data_beginBytes() + lastByteIndex)).byteValue()).charAt(a);
                                }
                                if (bitCheckIndex == 1) {
                                    oddBitCheckIndexValue = BytesToBinary.getBinaryString(((Byte)bitReservoir.get(bitReservoir.size() - frame.getMainDataBytes().size() - frame.getMain_data_beginBytes() + lastByteIndex)).byteValue()).charAt(a);
                                }
                                if (bitCheckIndex % 2 == 0 && BytesToBinary.getBinaryString(((Byte)bitReservoir.get(bitReservoir.size() - frame.getMainDataBytes().size() - frame.getMain_data_beginBytes() + lastByteIndex)).byteValue()).charAt(a) != evenBitCheckIndexValue) {
                                    ++numAncillaryBitAnomalies;
                                }
                                if (bitCheckIndex % 2 != 0 && BytesToBinary.getBinaryString(((Byte)bitReservoir.get(bitReservoir.size() - frame.getMainDataBytes().size() - frame.getMain_data_beginBytes() + lastByteIndex)).byteValue()).charAt(a) != oddBitCheckIndexValue) {
                                    ++numAncillaryBitAnomalies;
                                }
                                ++totalNumAncillaryBits;
                                ++bitCheckIndex;
                                if (ancillaryFrameIndex != 0 && ancillaryFrameIndex != 1) continue;
                                ++numAncillaryBitsInFirst2Frames;
                            }
                            for (a = bitReservoir.size() - frame.getMainDataBytes().size() - frame.getMain_data_beginBytes() + lastByteIndex + 1; a < nextMainDataBeginIndex; ++a) {
                                for (b = 0; b < 8; ++b) {
                                    bw.write(BytesToBinary.getBinaryString(((Byte)bitReservoir.get(a)).byteValue()).charAt(b));
                                    if (bitCheckIndex % 2 == 0 && BytesToBinary.getBinaryString(((Byte)bitReservoir.get(a)).byteValue()).charAt(b) != evenBitCheckIndexValue) {
                                        ++numAncillaryBitAnomalies;
                                    }
                                    if (bitCheckIndex % 2 != 0 && BytesToBinary.getBinaryString(((Byte)bitReservoir.get(a)).byteValue()).charAt(b) != oddBitCheckIndexValue) {
                                        ++numAncillaryBitAnomalies;
                                    }
                                    ++totalNumAncillaryBits;
                                    ++bitCheckIndex;
                                    if (ancillaryFrameIndex != 0 && ancillaryFrameIndex != 1) continue;
                                    ++numAncillaryBitsInFirst2Frames;
                                }
                            }
                        }
                        catch (IOException ex) {
                            Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (frameArrayList.get(frame.getId() + 1).getMain_data_beginBytes() > frame.getMainDataBytes().size()) {
                        try {
                            lastByteIndex = totalPart2_3_lengthBits / 8;
                            if (numAncillaryBitsInLastByte == 0) {
                                --lastByteIndex;
                            }
                            nextMainDataBeginIndex = bitReservoir.size() - frameArrayList.get(frame.getId() + 1).getMain_data_beginBytes() + frame.getMainDataBytes().size();
                            for (a = 8 - numAncillaryBitsInLastByte; a < 8; ++a) {
                                bw.write(BytesToBinary.getBinaryString(((Byte)bitReservoir.get(bitReservoir.size() - frame.getMain_data_beginBytes() + lastByteIndex)).byteValue()).charAt(a));
                                if (bitCheckIndex == 0) {
                                    evenBitCheckIndexValue = BytesToBinary.getBinaryString(((Byte)bitReservoir.get(bitReservoir.size() - frame.getMain_data_beginBytes() + lastByteIndex)).byteValue()).charAt(a);
                                }
                                if (bitCheckIndex == 1) {
                                    oddBitCheckIndexValue = BytesToBinary.getBinaryString(((Byte)bitReservoir.get(bitReservoir.size() - frame.getMain_data_beginBytes() + lastByteIndex)).byteValue()).charAt(a);
                                }
                                if (bitCheckIndex % 2 == 0 && BytesToBinary.getBinaryString(((Byte)bitReservoir.get(bitReservoir.size() - frame.getMain_data_beginBytes() + lastByteIndex)).byteValue()).charAt(a) != evenBitCheckIndexValue) {
                                    ++numAncillaryBitAnomalies;
                                }
                                if (bitCheckIndex % 2 != 0 && BytesToBinary.getBinaryString(((Byte)bitReservoir.get(bitReservoir.size() - frame.getMain_data_beginBytes() + lastByteIndex)).byteValue()).charAt(a) != oddBitCheckIndexValue) {
                                    ++numAncillaryBitAnomalies;
                                }
                                ++totalNumAncillaryBits;
                                ++bitCheckIndex;
                                if (ancillaryFrameIndex != 0 && ancillaryFrameIndex != 1) continue;
                                ++numAncillaryBitsInFirst2Frames;
                            }
                            for (a = bitReservoir.size() - frame.getMain_data_beginBytes() + lastByteIndex + 1; a < nextMainDataBeginIndex; ++a) {
                                for (b = 0; b < 8; ++b) {
                                    bw.write(BytesToBinary.getBinaryString(((Byte)bitReservoir.get(a)).byteValue()).charAt(b));
                                    if (bitCheckIndex % 2 == 0 && BytesToBinary.getBinaryString(((Byte)bitReservoir.get(a)).byteValue()).charAt(b) != evenBitCheckIndexValue) {
                                        ++numAncillaryBitAnomalies;
                                    }
                                    if (bitCheckIndex % 2 != 0 && BytesToBinary.getBinaryString(((Byte)bitReservoir.get(a)).byteValue()).charAt(b) != oddBitCheckIndexValue) {
                                        ++numAncillaryBitAnomalies;
                                    }
                                    ++totalNumAncillaryBits;
                                    ++bitCheckIndex;
                                    if (ancillaryFrameIndex != 0 && ancillaryFrameIndex != 1) continue;
                                    ++numAncillaryBitsInFirst2Frames;
                                }
                            }
                        }
                        catch (IOException ex) {
                            Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                ++ancillaryFrameIndex;
            }
            if (skipBitReservoirAdd) continue;
            bitReservoir.addAll(frame.getMainDataBytes());
        }
        try {
            bw.close();
        }
        catch (IOException ex) {
            Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
        }
        ByteWriter.writeBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits", totalNumAncillaryBits, numAncillaryBitsInFirst2Frames, "ANC");
        if (totalNumAncillaryBits > 0) {
            int numStegazaurusLocationBits = 0;
            int covertDataBeginIndex = 0;
            numStegazaurusLocationBits = (int)(Math.log(totalNumAncillaryBits) / Math.log(2.0));
            if (Math.log(totalNumAncillaryBits) / Math.log(2.0) - (double)numStegazaurusLocationBits > 0.0) {
                ++numStegazaurusLocationBits;
            }
            covertDataBeginIndex = numAncillaryBitsInFirst2Frames + numStegazaurusLocationBits;
            try {
                int numberOfBitsToBeWritten;
                if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32).equals("11111111110110001111111111100000")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(numAncillaryBitsInFirst2Frames, numAncillaryBitsInFirst2Frames + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Extracted_JPEG_Image.jpg", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** JPEG file found and extracted from Ancillary Bits! ***";
                } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32).equals("10001001010100000100111001000111")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(numAncillaryBitsInFirst2Frames, numAncillaryBitsInFirst2Frames + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Extracted_PNG_Image.png", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** PNG file found and extracted from Ancillary Bits! ***";
                } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 24).equals("010001110100100101000110")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(numAncillaryBitsInFirst2Frames, numAncillaryBitsInFirst2Frames + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Extracted_GIF_Image.gif", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** GIF file found and extracted from Ancillary Bits! ***";
                } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 16).equals("0100001001001101")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(numAncillaryBitsInFirst2Frames, numAncillaryBitsInFirst2Frames + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Extracted_BMP_Image.bmp", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** BMP file found and extracted from Ancillary Bits! ***";
                } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 24).equals("010010010100100100101010")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(numAncillaryBitsInFirst2Frames, numAncillaryBitsInFirst2Frames + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Extracted_TIFF_Image.tif", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** TIFF file found and extracted from Ancillary Bits! ***";
                } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 16).equals("1111111111111010")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(numAncillaryBitsInFirst2Frames, numAncillaryBitsInFirst2Frames + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** MP3 file found and extracted from Ancillary Bits! ***";
                } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 16).equals("1111111111111011")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(numAncillaryBitsInFirst2Frames, numAncillaryBitsInFirst2Frames + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** MP3 file found and extracted from Ancillary Bits! ***";
                } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 24).equals("010010010100010000110011")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(numAncillaryBitsInFirst2Frames, numAncillaryBitsInFirst2Frames + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Extracted_MP3_Audio.mp3", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** MP3 file found and extracted from Ancillary Bits! ***";
                } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 16).equals("0101000001001011")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(numAncillaryBitsInFirst2Frames, numAncillaryBitsInFirst2Frames + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Extracted_ZIP_Archive.zip", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** ZIP file found and extracted from Ancillary Bits! ***";
                } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32).equals("01010010011000010111001000100001")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(numAncillaryBitsInFirst2Frames, numAncillaryBitsInFirst2Frames + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Extracted_RAR_Archive.rar", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** RAR file found and extracted from Ancillary Bits! ***";
                } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 32).equals("00100101010100000100010001000110")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(numAncillaryBitsInFirst2Frames, numAncillaryBitsInFirst2Frames + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Extracted_PDF_Document.pdf", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** PDF file found and extracted from Ancillary Bits! ***";
                } else if (new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + 24).equals("010000010100010101010011")) {
                    numberOfBitsToBeWritten = BinaryToDecimal.getDecimalNumber(new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(numAncillaryBitsInFirst2Frames, numAncillaryBitsInFirst2Frames + numStegazaurusLocationBits));
                    ByteWriter.writeDataBytesToFile("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Extracted_AES_Encrypted_File.aes", new BufferedReader(new FileReader("Extracted_Information\\" + filePath.getName().toString() + "\\AncillaryBits\\Bit.txt")).readLine().substring(covertDataBeginIndex, covertDataBeginIndex + numberOfBitsToBeWritten));
                    foundFileString = "*** AES file found and extracted from Ancillary Bits! ***";
                }
            }
            catch (FileNotFoundException ex) {
                Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex) {
                Logger.getLogger(StegaProb.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("\n\n\n---------------------- Ancillary Bit Scan Findings ----------------------");
        System.out.println("\nNumber of suspect Ancillary bits in file:    " + totalNumAncillaryBits);
        System.out.println("Number of anomalous Ancillary bits detected: " + numAncillaryBitAnomalies);
        System.out.print("\nAnomalous Ancillary bit percentage:          ");
        if (totalNumAncillaryBits == 0 && numAncillaryBitAnomalies == 0) {
            System.out.println("0%");
        } else {
            System.out.printf("%1$.2f", (double)numAncillaryBitAnomalies / (double)totalNumAncillaryBits * 100.0);
            System.out.println("%");
        }
        System.out.println("\nFiles now available in the Extracted_Information directory");
        System.out.println("representing all extracted information in bit and byte formats.");
        System.out.println("\n" + foundFileString + "\n");
    }
 
}