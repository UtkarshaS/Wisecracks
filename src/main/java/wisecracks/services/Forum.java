package main.java.wisecracks.services;

class Forum{
  String forumName;
  float radiusInMiles = 1; // default value for radius
  String creator;
  
  //Primary Constructor:
  public Forum(String fname, int r, String c_user){
    radiusInMiles = r;
    forumName = fname;
    creator = c_user;
  }
  
  //Secondary Constructor:(when radius not specified)
  Forum(String fname, int r, String c_user){
    forumName = fname;
    creator = c_user;
  }
  
  void checkIfForumExists(){
    //Search DB for forumName //forumName will be unique- Primary Id
  }
  
  boolean deleteForum(){
    //Connection to DB, if not connected 
    //If connected, call checkIfForumExists 
    // If exists, delete from Database //RDS
  }
  
  boolean createForumSubscription(int accessLevel){
    //SNS Implementation
  }
  
  boolean deleteForumSubscription(){
    //SNS Implementation
  }
  
}
