package wdd.musicplayer.model;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by gengzhibo on 2017/11/10.
 */

public class Music implements Serializable {
    public String name , artist , path , filename ;
    public Long ALBUM_ID , id ;
    public int longTime;
    public boolean favorite = false , isPlaying = false;
//    public int mode = 0;
}
