<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
require_once "DB.php";

// Quick en dirty php rest api
$db = new DB();

if($_SERVER['REQUEST_METHOD'] == "GET") { // GET DATA
	
	if($_GET['url'] == "auth") {
		
	} else if($_GET['url'] == "adverts") {
		// Get all adverts
		$getAdverts = $db->query('SELECT adverts.id, user_id, advert_name, advert_description, '.
		                         'user.name as "user_name", category_advert.category_name '.
		                         'FROM adverts '.
		                         'LEFT JOIN user ON adverts.user_id = user.id '.
		                         'LEFT JOIN category_advert on advert_category_id = category_advert.id');
		header("Content-Type: application/json");
		echo json_encode($getAdverts);
		http_response_code(200); //standaard
	}	
} else if($_SERVER['REQUEST_METHOD'] == "POST" && $_GET['url'] == "auth") { // POST DATA
	login($db);
} else if($_SERVER['REQUEST_METHOD'] == "DELETE" && $_GET['url'] == "auth"){ // Remove login token
	removeLoginToken($db);
} else if($_SERVER['REQUEST_METHOD'] == "POST" && $_GET['url'] == "tokenCheck"){ // Check token
	checkToken($db);
} else {
	http_response_code(405);
}



// Login en maak auth token aan in db return token in json
function login($db){
	$postBody = file_get_contents("php://input");
	$postBody = json_decode($postBody);
	
	$email = $postBody->email;
	$password = $postBody->password;
	
	if($db->query('SELECT email FROM user WHERE email = :email', array(':email'=>$email))) {
		if(password_verify($password, $db->query('SELECT password FROM user WHERE email = :email',
		                                         array(':email'=>$email))[0]['password'])) {
			$csStrong = true;
			$token = bin2hex(openssl_random_pseudo_bytes(64, $csStrong));
			$user_id = $db->query('SELECT id FROM user WHERE email=:email', array(':email'=>$email))[0]['id'];
			
			$db->query('INSERT INTO login_tokens VALUES (NULL, :token, :user_id)',
			                                            array(':token'=>sha1($token), ':user_id'=>$user_id));
			header("Content-type:application/json");
			echo '{  "User_id": "'.$user_id.'", "Token": "'.$token.'" }';
		} else {
			http_response_code(401);
		}
	} else {
		//echo password_hash("goeieww01", PASSWORD_BCRYPT); // create passwd
		http_response_code(401);
	}
}

// Delete login token
function removeLoginToken($db){
	$getToken = sha1($_GET['token']);
	if(isset($_GET['token']) && $db->query("SELECT token FROM login_tokens WHERE token = :token", 
	                                       array('token'=>$getToken))){
		$db->query('DELETE FROM login_tokens WHERE token = :token', array('token'=>$getToken));
		echo '{ "Status": "Success" }';
	} else {
		echo '{ "Error" : "Wrong request" }';
		http_response_code(400);
	}
}

// check token
function checkToken($db){
	$postBody = file_get_contents("php://input");
	$postBody = json_decode($postBody);
	
	$email = $postBody->email;
	$authToken = sha1($postBody->authToken);
	
	if($db->query('SELECT email FROM user WHERE email = :email', array(':email'=>$email))) {
		header("Content-type:application/json");
		echo '{ "User" : "'.$email.'", ';
		if($db->query('SELECT token FROM login_tokens WHERE token = :token', array('token'=>$authToken))) {
			echo ' "TokenStatus" : "true" }';
		} else {
			echo ' "TokenStatus" : "false" }';
		}
	}
}

?>