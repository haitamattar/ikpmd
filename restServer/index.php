<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);
require_once "DB.php";

// Quick en dirty php rest api
$db = new DB('127.0.0.1', "freeOb", "root", "");

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
} else if($_SERVER['REQUEST_METHOD'] == "POST" && $_GET['url'] == "userProfile") { // POST DATA
	ownProfileInformation($db);
} else if($_SERVER['REQUEST_METHOD'] == "POST" && $_GET['url'] == "register") { // POST DATA
	registerUser($db);
} else if($_SERVER['REQUEST_METHOD'] == "DELETE" && $_GET['url'] == "auth"){ // Remove login token
	removeLoginToken($db);
} else if($_SERVER['REQUEST_METHOD'] == "POST" && $_GET['url'] == "tokenCheck"){ // Check token
	checkToken($db);
} else if($_SERVER['REQUEST_METHOD'] == "POST" && $_GET['url'] == "addAdvert"){ // Add advert
	if(checkRequestToken($db)){
		addAdvert($db);
	} else {
		http_response_code(401);
		die();
	}
} else {
	http_response_code(405);
}


// Login en maak auth token aan in db return token in json
// IN: db connection
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
			$user_id = $db->query('SELECT id, email FROM user WHERE email=:email', array(':email'=>$email))[0];
			
			$db->query('INSERT INTO login_tokens VALUES (NULL, :token, :user_id)',
			                                            array(':token'=>sha1($token), ':user_id'=>$user_id['id']));
			
			header("Content-type:application/json");
			echo '{  "User_id": "'.$user_id['id'].'", "Email": "'.$user_id['email'].'","AuthToken": "'.$token.'" }';
		} else {
			http_response_code(401);
		}
	} else {
		http_response_code(401);
	}
}


// Delete login token
// IN: db connection
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


// Add an advert
// IN: db connection
function addAdvert($db){
	$postBody = file_get_contents("php://input");
	$postBody = json_decode($postBody);

	$name = $postBody->advert_name;
	$descr = $postBody->description;
	$category_id = $postBody->category;
	$user_id = $db->query('SELECT id FROM user WHERE email=:email', array(':email'=>$postBody->email))[0]['id'];

	$db->query( "INSERT INTO adverts (user_id, advert_name, advert_description, advert_category_id)".
				"VALUES (:uid, :an, :ad, :acid)",
				array(':uid'=>$user_id, ':an'=>$name, ':ad'=>$descr, ':acid'=>$category_id));
	echo '{ "Status": "Success" }';
}


// check token
// IN: db connection
function checkToken($db){
	$postBody = file_get_contents("php://input");
	$postBody = json_decode($postBody);
	
	$email = $postBody->email;
	$authToken = sha1($postBody->authToken);
	$authTokenInserted = $postBody->authToken;
	
	if($db->query('SELECT email FROM user WHERE email = :email', array(':email'=>$email))) {
		$user_id = $db->query('SELECT id, email FROM user WHERE email=:email', array(':email'=>$email))[0];
		if($db->query('SELECT token FROM login_tokens WHERE token = :token', array('token'=>$authToken))) {
			header("Content-type:application/json");
			echo '{ "User_id" : "'.$user_id['id'].'", ';
			echo '  "Email" : "'.$user_id['email'].'", ';
			echo '  "AuthToken" : "'.$authTokenInserted.'", ';
			echo ' "TokenStatus" : "true" }';
		} else {
			http_response_code(401);
		}
	}
}


// Check token for certain requests
// IN: db connection
// OUT: true or false
function checkRequestToken($db){
	$postBody = file_get_contents("php://input");
	$postBody = json_decode($postBody);
	
	$email = $postBody->email;
	$authToken = sha1($postBody->authToken);
	$authTokenInserted = $postBody->authToken;

	if($db->query('SELECT email FROM user WHERE email = :email', array(':email'=>$email))) {
		$user_id = $db->query('SELECT id, email FROM user WHERE email=:email', array(':email'=>$email))[0];
		if($db->query('SELECT token FROM login_tokens WHERE token = :token', array('token'=>$authToken))) {
			return true;
		} else {
			return false;
		}
	}
}


// Register user (create an account)
// IN: db connection
function registerUser($db){
	$postBody = file_get_contents("php://input");
	$postBody = json_decode($postBody);
	$name = $postBody->name;
	$email = $postBody->email;
	$password = password_hash($postBody->password, PASSWORD_BCRYPT);
	
	try {
	$db->query("INSERT INTO `user` (`id`, `name`, `email`, `password`) VALUES (NULL, :name, :email, :password)", 
	           array(':name'=>$name, "email"=>$email, "password"=>$password));
	
	$csStrong = true;
	$token = bin2hex(openssl_random_pseudo_bytes(64, $csStrong));
	$user_id = $db->query('SELECT id, email FROM user WHERE id = id', array(':id'=>$db->lastInsertId()))[0];
	
	$db->query('INSERT INTO login_tokens VALUES (NULL, :token, :user_id)',
	                                            array(':token'=>sha1($token), ':user_id'=>$user_id['id']));
		
	header("Content-type:application/json");
	echo '{  "User_id": "'.$user_id['id'].'", "Email": "'.$user_id['email'].'","AuthToken": "'.$token.'" }';
	
	}
	catch(PDOException $e) {
		if ($e->errorInfo[1] == 1062) {
			// echo "email bestaat al";
			http_response_code(400);
			header("Content-type:application/json");
			echo '{ "Error" : "Email already exists" }';
		} else {
		http_response_code(400); // wrong credit
		die();
		}
	}
}


//Show user profile
function ownProfileInformation($db){
	
	if(checkRequestToken($db)){
		$postBody = file_get_contents("php://input");
		$postBody = json_decode($postBody);
		$userId = $postBody->userId;
		
		if(trim($userId) == ""){
			http_response_code(400); // wrong credit
			die();
		}
		
		$userAndAdverts["adverts"] = $db->query("SELECT `user`.`id`, `name`, `latitude`, `longitude`, `bio`,".
		                                        " `adverts`.`id` as 'advert_id', `adverts`.`advert_name` " .
		                                        "FROM `user` " .
		                                        "LEFT JOIN `adverts` ON `adverts`.`user_id` = `user`.`id` " .
		                                        "WHERE `user`.`id` = :id", array('id'=>$userId));
		header("Content-Type: application/json");
		$user = ["Name"=>$userAndAdverts["adverts"][0]["name"],
		         "Bio"=>$userAndAdverts["adverts"][0]["bio"],
		         "Lat"=>$userAndAdverts["adverts"][0]["latitude"], "Lon"=>$userAndAdverts["adverts"][0]["longitude"]];
		
		$userAndAdverts["User"] = $user; 
		
		echo json_encode($userAndAdverts);
	} else {
		http_response_code(401);// No access
	}
}

?>