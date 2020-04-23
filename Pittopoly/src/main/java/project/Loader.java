package project;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;

import java.nio.file.Paths;

public class Loader {

  Storage storage;
  String bucketName;

  Loader(Storage storage, String bucketName) {
    this.storage = storage;
    this.bucketName = bucketName;
  }

  public void loadPlayers(String id) {
    Blob blob = storage.get(BlobId.of(bucketName, "player-" + id));
    blob.downloadTo(Paths.get("playersaves.txt"));
  }

  public void loadBoard(String id) {
    Blob blob = storage.get(BlobId.of(bucketName, "board-" + id));
    blob.downloadTo(Paths.get("boardsaves.txt"));
  }

}
