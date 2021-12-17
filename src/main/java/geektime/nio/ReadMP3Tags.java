package geektime.nio;

import java.io.*;
import java.nio.*;
import java.nio.file.*;
import static java.nio.file.StandardOpenOption.*;
import java.nio.channels.*;

public class ReadMP3Tags {
 
    public void read(String filePath) {
        Path file = Paths.get(filePath);
        ByteBuffer buffer = null;
 
        try (FileChannel fc = FileChannel.open(file, READ))
        {
            fc.position(fc.size() - 128);
            buffer = ByteBuffer.allocate(3);
 
            do {
                fc.read(buffer);
            } while (buffer.hasRemaining());
 
            String tag = new String(buffer.array());
 
            if (!"TAG".equals(tag)) {
                System.out.println("This file doesn't support ID3v1");
                System.exit(0);
            }
 
            buffer = ByteBuffer.allocate(30);
 
            do {
                fc.read(buffer);
            } while (buffer.hasRemaining());
 
            String songName = new String(buffer.array());
 
            System.out.println("Song name: " + songName);
 
            buffer.clear();
 
            do {
                fc.read(buffer);
            } while (buffer.hasRemaining());
 
            String artist = new String(buffer.array());
 
            System.out.println("Artist: " + artist);
 
            buffer.clear();
 
            do {
                fc.read(buffer);
            } while (buffer.hasRemaining());
 
            String album = new String(buffer.array());
 
            System.out.println("Album: " + artist);
 
 
            buffer = ByteBuffer.allocate(4);
 
            do {
                fc.read(buffer);
            } while (buffer.hasRemaining());
 
            String year = new String(buffer.array());
 
            System.out.println("Year: " + year);
 
        } catch (IOException ex) {
            System.err.println("I/O Error: " + ex);
        }
    }
 
    public static void main(String[] args) {
        String filePath = args[0];
        new ReadMP3Tags().read(filePath);
    }
}
