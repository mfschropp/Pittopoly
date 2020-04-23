package project;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.*;
import org.mockito.Mock;

import java.io.FileWriter;
import java.nio.file.Paths;

@SpringBootTest
@ActiveProfiles("test")
class SaverTests {

}
