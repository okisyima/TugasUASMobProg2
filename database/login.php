<?php

include_once 'koneksi.php';

$response = array("error" => FALSE);

if (isset($_POST['nickname']) && isset($_POST['password'])) {

    $username = htmlspecialchars($_POST['nickname']);
    $password = htmlspecialchars($_POST['password']);


    $sql = $conn->query("SELECT * FROM users WHERE nickname='$nickname' AND password='$password'");

    if (mysqli_num_rows($sql) > 0) {
        while ($row = $sql->fetch_array()) {
            $response["error"] = FALSE;
            $response["message"] = "Login Berhasil";
            $response["id"] = $row['id'];
            $response["nickname"] = $row['nickname'];
            $response["success"] = 1;
        }

        echo json_encode($response);
    } else {
        $response["error"] = TRUE;
        $response["message"] = "nickname atau password salah!!";
        $response["success"] = 0;

        echo json_encode($response);
    }
}
