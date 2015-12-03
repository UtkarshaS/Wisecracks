class Post{
  int post_id;
  String post_content;
  int user;
  
  //Creates new post
  Post(int user_id, String content){
    post_id = previous_post_id +1 ; //fetch from database dynamically
    user = user_id;
    post_content = content;
  }
  
  //Retrive content of the post by Id
  String getPostContent(int post_id){
    //embedded sql query OR procedure
    return result;
  }
  
  //Retrive Id by content of Post NOTE: full content is required. Partial content search is tricky!
  int getPostId(String content){
    //embedded sql query OR procedure
    return result; 
  }
  
  //Modifies Post content
  boolean modifyPost(String new Content){
    //NOTE: Android guys > make sure to keep the previous content in the text box for editting and then send the new content.
    
    try{
    //embedded sql query OR procedure //REPLACE POST.CONTENT> old to new 
      return true;
    }
    catch(SQLException e){
      return false;
    }
  }
  
  //Delete a post from the database
  //NOTE: Bug> will allow anybody to delete this entry > find a way to retrict to user
  boolean deletePost(int post_id){
    try{
    //sql query to delete record from DB
      return true;
    }
    
    //NOTE: to self > remember to handle IOEXception by re-checking DB and then returning result
    catch(SQLException){
      return false;
    }
  }
}
