package project;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Saver {

  Player[] players;
  BoardSpace[] board;
  Storage storage;
  String bucketName;

  Saver(Player[] players, BoardSpace[] board, Storage storage, String bucketName) {
    this.players = players;
    this.board = board;
    this.storage = storage;
    this.bucketName = bucketName;
  }

  public void writeToSave(String fileName) {
    try {
      FileWriter writer = new FileWriter("player-" + fileName);
      writeToPlayerFile(writer);
      writer = new FileWriter("board-" + fileName);
      writeToBoardFile(writer);
      save(fileName);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void writeToPlayerFile(FileWriter writer) {
    try {
      String end = ", ";
      for (Player player : players) {
        writer.write(player.getName() + end);
        writer.write(player.getLocation() + end);
        writer.write(player.getBalance() + end);
        writer.write(player.getPlayerNum() + end);
        if (player.getJail()) {
          writer.write("1" + end);
        } else {
          writer.write("0" + end);
        }
        if (player.jailFreeCard()) {
          writer.write("1" + end + player.getJailAttempt());
        } else {
          writer.write("0" + end + player.getJailAttempt());
        }
        writer.write(System.lineSeparator());
      }
      writer.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void writeToBoardFile(FileWriter writer) {
    try {
      String end = ", ";
      for (BoardSpace space : board) {
        writer.write(space.getName() + end);
        writer.write(space.getType() + end);
        writer.write(space.getCost() + end);
        writer.write(space.getOriginalRent() + end);
        writer.write(space.getRent() + end);
        if (space.getOwner() != null) {
          writer.write(space.getOwner().getName() + end);
        } else {
          writer.write("none" + end);
        }
        writer.write(space.getNumHouses() + end + space.getNumHotels());
        writer.write(System.lineSeparator());
      }
      writer.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void save(String fileName) {
    BlobId blobId = BlobId.of(bucketName, "player-" + fileName);
    BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();

    try {
      storage.create(blobInfo, Files.readAllBytes(Paths.get("player-" + fileName)));
    } catch (Exception e) {
      System.out.println("Could not save player file: " + e);
    }

    blobId = BlobId.of(bucketName, "board-" + fileName);
    blobInfo = BlobInfo.newBuilder(blobId).build();

    try {
      storage.create(blobInfo, Files.readAllBytes(Paths.get("board-" + fileName)));
    } catch (Exception e) {
      System.out.println("Could not save board file: " + e);
    }
  }
}
