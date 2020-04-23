package project;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.*;
import org.mockito.Mock;

import java.nio.file.Paths;

@SpringBootTest
@ActiveProfiles("test")
class LoaderTests {

  Loader loader;
  Storage storage;
  String bucketName;
  Blob blob;

  @BeforeEach
  void setup() {
    storage = mock(Storage.class);
    blob = mock(Blob.class);
    when(storage.get(any(BlobId.class))).thenReturn(blob);
    bucketName = "test";
    loader = new Loader(storage, bucketName);
  }

  @Test
  void testLoadPlayers() {
    loader.loadPlayers("test");
    verify(blob, times(1)).downloadTo(any());
  }

  @Test
  void testLoadBoard() {
    loader.loadBoard("test");
    verify(blob, times(1)).downloadTo(any());
  }

}
