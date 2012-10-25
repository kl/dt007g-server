package chatserver;

import java.net.InetAddress;

public class Friend
{
  private String nickname;
  private InetAddress address;
  private String fullname;
  private String image;
  private String time;

  public Friend(String nickname, String fullname, InetAddress address, String image, String time)
  {
    this.nickname = nickname;
    this.address = address;
    this.fullname = fullname;
    this.image = image;
    this.time = time;
  }

  public String getNickname()
  {
    return this.nickname;
  }

  public void setNickname(String nickname)
  {
    this.nickname = nickname;
  }

  public InetAddress getAddress()
  {
    return this.address;
  }

  public void setAddress(InetAddress address)
  {
    this.address = address;
  }

  public String getFullname()
  {
    return this.fullname;
  }

  public void setFullname(String fullname)
  {
    this.fullname = fullname;
  }

  public String getImage()
  {
    return this.image;
  }

  public void setImage(String image)
  {
    this.image = image;
  }

  public String getTime()
  {
    return this.time;
  }

  public void setTime(String time)
  {
    this.time = time;
  }
}