<?php
include 'koneksi.php';

class usr
{ }

$nickname = $_POST["nickname"];
$password = $_POST["password"];
$confirm_password = $_POST["confirm_password"];

if ((empty($nickname))) {
  $response = new usr();
  $response->success = 0;
  $response->message = "Kolom nickname tidak boleh kosong";
  die(json_encode($response));
} else if ((empty($password))) {
  $response = new usr();
  $response->success = 0;
  $response->message = "Kolom password tidak boleh kosong";
  die(json_encode($response));
} else if ((empty($confirm_password)) || $password != $confirm_password) {
  $response = new usr();
  $response->success = 0;
  $response->message = "Konfirmasi password tidak sama";
  die(json_encode($response));
} else {
  if (!empty($nickname) && $password == $confirm_password) {
    $num_rows = mysql_num_rows(mysql_query("SELECT * FROM user WHERE nickname='" . $nickname . "'"));

    if ($num_rows == 0) {
      $query = mysql_query("INSERT INTO user (id, nickname, password) VALUES(0,'" . $nickname . "','" . $password . "')");

      if ($query) {
        $response = new usr();
        $response->success = 1;
        $response->message = "Register berhasil, silahkan login.";
        die(json_encode($response));
      } else {
        $response = new usr();
        $response->success = 0;
        $response->message = "nickname sudah ada";
        die(json_encode($response));
      }
    } else {
      $response = new usr();
      $response->success = 0;
      $response->message = "nickname sudah ada";
      die(json_encode($response));
    }
  }
}

mysql_close();
