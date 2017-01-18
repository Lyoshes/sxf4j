package org.cleanlogic.sxf4j.io;

import org.cleanlogic.sxf4j.SXF;
import org.cleanlogic.sxf4j.exceptions.SXFWrongFormatException;
import org.cleanlogic.sxf4j.format.SXFDescriptor;
import org.cleanlogic.sxf4j.format.SXFPassport;
import org.cleanlogic.sxf4j.format.SXFRecord;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class of access to SXF file format.
 * @author Serge Silaev aka iSergio <s.serge.b@gmail.com>
 */
public class SXFReader {
    private final SXFReaderOptions _sxfReaderOptions;
    private String _filePath;
    private SXFPassport _sxfPassport;
    private SXFDescriptor _sxfDescriptor;
    private List<SXFRecord> _sxfRecords = new ArrayList<>();

    /**
     * Default constructor for SXF read
     */
    public SXFReader() {
        _sxfReaderOptions = new SXFReaderOptions();
    }

    public SXFReader(SXFReaderOptions readerOptions) {
        _sxfReaderOptions = readerOptions;
    }

    /**
     * Create SXF reader with file path which will be readed.
     * You not need call {@link SXFReader#read(String)} function.
     * If you call {@link #read(String)} function, file read from begin.
     * @param filePath file path to SXF
     * @throws IOException
     */
    public SXFReader(String filePath, SXFReaderOptions sxfReaderOptions) throws IOException, SXFWrongFormatException {
        _sxfReaderOptions = sxfReaderOptions;
        read(filePath);
    }

    /**
     * Create SXF reader with file path which will be readed.
     * You not need call {@link SXFReader#read(String)} function.
     * If you call {@link #read(String)} function, file read from begin.
     * @param file file for read SXF
     * @throws IOException
     */
    public SXFReader(File file, SXFReaderOptions sxfReaderOptions) throws IOException, SXFWrongFormatException {
        _sxfReaderOptions = sxfReaderOptions;
        read(file);
    }

    /**
     * Read SXF file by path
     * @param filePath file path to SXF
     * @throws IOException
     */
    public void read(String filePath) throws IOException, SXFWrongFormatException {
        _filePath = filePath;
        RandomAccessFile raf = new RandomAccessFile(filePath, "r");
        read(raf);
    }

    public void read(File file) throws IOException, SXFWrongFormatException {
        _filePath = file.getPath();
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        read(raf);
    }

    private void read(RandomAccessFile raf) throws IOException, SXFWrongFormatException {
        if (_sxfPassport != null) {
            _sxfPassport = null;
        }

        MappedByteBuffer mappedByteBuffer = raf.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, raf.length());
        mappedByteBuffer.order(ByteOrder.LITTLE_ENDIAN);

        int identifier = mappedByteBuffer.getInt();
        if (identifier != SXF.FILE_SXF) {
            throw new SXFWrongFormatException("File " + _filePath + " NOT SXF FORMAT");
        }

        // Read passport of SXF
        SXFPassportReader sxfPassportReader = new SXFPassportReader(mappedByteBuffer, _sxfReaderOptions);
        _sxfPassport = sxfPassportReader.read();

        SXFDescriptorReader sxfDescriptorReader = new SXFDescriptorReader(_sxfPassport);
        _sxfDescriptor = sxfDescriptorReader.read();

        SXFRecordReader sxfRecordReader = new SXFRecordReader(_sxfPassport, _sxfDescriptor);
        _sxfRecords = sxfRecordReader.read();
//
//        // Check for metaobject is exists. It will be first object after descriptor
//        if (_sxfRecords.size() > 0) {
//            SXFRecord sxfRecord = _sxfRecords.get(0);
//            // Metaobject mast have excode = 0 and nuber = 0.
//            if (sxfRecord.getHeader().excode == 0 && sxfRecord.getHeader().number == 0) {
//                System.out.println("Metaobject finded! TODO!");
//            }
//        }

        raf.close();
    }

    /**
     * Get read SXF passport of SXF
     * @return SXF passport
     */
    public SXFPassport getPassport() {
        return _sxfPassport;
    }

    public SXFDescriptor getDescriptor() {
        return _sxfDescriptor;
    }

    public List<SXFRecord> getRecords() {
        return _sxfRecords;
    }

    public SXFRecord getRecordByIncode(int incode) {
        return _sxfRecords.get(incode);
    }

    public List<SXFRecord> getRecordByExcode(int excode) {
        List<SXFRecord> result = new ArrayList<>();
        for (SXFRecord sxfRecord : _sxfRecords) {
            if (sxfRecord.getHeader().excode == excode) {
                result.add(sxfRecord);
            }
        }
        return result;
    }

    public SXFRecord getRecordByNumber(int number) {
        for (SXFRecord sxfRecord : _sxfRecords) {
            if (sxfRecord.getHeader().number == number) {
                return sxfRecord;
            }
        }
        return null;
    }
}
