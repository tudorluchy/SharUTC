package fr.utc.lo23.sharutc.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main object of the application, contains all relatives informations about
 * the music file in memory via the catalogs.
 *
 * id3tags are parsed when the music is added locally, user changes are reported
 * to the file also, a copy of these informations is stored with the Music
 * object to enhance search trough a list of musics
 *
 */
public class Music implements Serializable {
    
    private static final Logger log = LoggerFactory.getLogger(Music.class);
    private static final long serialVersionUID = 6722258623736849911L;
    @JsonIgnore
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private Long mId;
    private String mFileName;
    private String mRealName;
    /**
     * No JsonIgnore annotation, but when searching a music, file must be set to
     * null before returning the results, file must be present for transfers
     */
    private Byte[] mFile;
    private Boolean mFileMissing;
    private Long mOwnerPeerId;
    private Integer mHash;
    private Set<Integer> mCategoryIds;
    private String mTitle;
    private String mArtist;
    private String mAlbum;
    private String mTrack;
    private Integer mTrackLength;
    private Integer mYear;
    private List<Comment> mComments;
    private Set<Score> mScores;
    private Set<String> mTags;
    private Boolean mMayReadInfo;
    private Boolean mMayListen;
    private Boolean mMayCommentAndNote;

    /**
     *
     */
    public Music() {
    }

    /**
     * Clone for sending result list
     *
     * @param music
     */
    public Music(Music music) {
        
        this.mId = new Long(music.mId);
        this.mFileName = music.mFileName;
        this.mRealName = music.mRealName;
        if (music.getFile() != null) {
            this.mFile = new Byte[music.getFile().length];
            System.arraycopy(music.mFile, 0, this.mFile, 0, music.getFile().length);
        } else {
            this.mFile = null;
        }
        this.mFileMissing = music.mFileMissing;
        this.mOwnerPeerId = new Long(music.mOwnerPeerId);
        this.mHash = new Integer(music.mHash);
        
        this.mCategoryIds = new HashSet<Integer>();
        for (Integer i : music.mCategoryIds) {
            this.mCategoryIds.add(new Integer(i));
        }
        this.mTitle = music.mTitle;
        this.mArtist = music.mArtist;
        this.mAlbum = music.mAlbum;
        this.mTrack = music.mTrack;
        this.mTrackLength = new Integer(music.mTrackLength);
        this.mYear = new Integer(music.mYear);
        this.mComments = new ArrayList<Comment>();
        // using a clone to not erase 
        for (Comment comment : music.mComments) {
            this.mComments.add(comment.clone());
        }
        this.mScores = new HashSet<Score>();
        for (Score score : music.mScores) {
            this.mScores.add(score);
        }
        this.mTags = music.mTags;
        this.mMayReadInfo = music.mMayReadInfo;
        this.mMayListen = music.mMayListen;
        this.mMayCommentAndNote = music.mMayCommentAndNote;
    }

    /**
     * To use for importing a new local file
     *
     * @param id
     * @param ownerPeerId
     * @param file
     * @param fileName
     * @param title
     * @param artist
     * @param album
     * @param track
     * @param trackLength
     */
    public Music(Long id, Long ownerPeerId, Byte[] file, String fileName, String realName, Integer hashcode, String title, String artist, String album, String track, Integer trackLength) {
        this.mId = id;
        this.mFileName = fileName;
        this.mRealName = realName;
        this.mFileMissing = false;
        this.mOwnerPeerId = ownerPeerId;
        this.mHash = hashcode;
        this.mCategoryIds = new HashSet<Integer>();
        this.mFile = file;
        
        this.mComments = new ArrayList<Comment>();
        this.mScores = new HashSet<Score>();
        this.mTags = new HashSet<String>();
        this.mTitle = title;
        this.mArtist = artist;
        this.mAlbum = album;
        this.mTrack = track;
        this.mTrackLength = trackLength;
        this.mMayReadInfo = null;
        this.mMayListen = null;
        this.mMayCommentAndNote = null;
    }

    /**
     *
     * @return
     */
    public Long getId() {
        return mId;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.mId = id;
    }

    /**
     *
     * @return
     */
    public String getFileName() {
        return mFileName;
    }

    /**
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        String oldFileName = this.mFileName;
        this.mFileName = fileName;
        propertyChangeSupport.firePropertyChange(Property.FILENAME.name(), oldFileName, fileName);
    }

    /**
     *
     * @return
     */
    public String getRealName() {
        return mRealName;
    }

    /**
     *
     * @param realName
     */
    public void setRealName(String realName) {
        this.mRealName = realName;
    }

    /**
     *
     * @return
     */
    public Byte[] getFile() {
        return mFile;
    }

    /**
     *
     * @param file
     */
    public void setFile(Byte[] file) {
        this.mFile = file;
    }

    /**
     *
     * @return
     */
    public Boolean getFileMissing() {
        return mFileMissing;
    }

    /**
     *
     * @param fileMissing
     */
    public void setFileMissing(Boolean fileMissing) {
        this.mFileMissing = fileMissing;
        propertyChangeSupport.firePropertyChange(Property.FILE_MISSING.name(), !fileMissing.booleanValue(), fileMissing.booleanValue());
    }

    /**
     *
     * @return
     */
    public Long getOwnerPeerId() {
        return mOwnerPeerId;
    }

    /**
     *
     * @param ownerPeerId
     */
    public void setOwnerPeerId(Long ownerPeerId) {
        this.mOwnerPeerId = ownerPeerId;
    }

    /**
     *
     * @return
     */
    public Integer getHash() {
        return mHash;
    }

    /**
     *
     * @param hash
     */
    public void setHash(Integer hash) {
        this.mHash = hash;
    }

    /**
     *
     * @return
     */
    public Set<Integer> getCategoryIds() {
        return mCategoryIds;
    }

    /**
     *
     * @param categoryIds
     */
    public void setCategoryIds(Set<Integer> categoryIds) {
        this.mCategoryIds = categoryIds;
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        String oldTitle = this.mTitle;
        this.mTitle = title;
        propertyChangeSupport.firePropertyChange(Property.TITLE.name(), oldTitle, title);
    }

    /**
     *
     * @return
     */
    public String getArtist() {
        return mArtist;
    }

    /**
     *
     * @param artist
     */
    public void setArtist(String artist) {
        String oldArtist = this.mArtist;
        this.mArtist = artist;
        propertyChangeSupport.firePropertyChange(Property.ARTIST.name(), oldArtist, artist);
    }

    /**
     *
     * @return
     */
    public String getAlbum() {
        return mAlbum;
    }

    /**
     *
     * @param album
     */
    public void setAlbum(String album) {
        String oldAlbum = this.mAlbum;
        this.mAlbum = album;
        propertyChangeSupport.firePropertyChange(Property.ALBUM.name(), oldAlbum, album);
    }

    /**
     *
     * @return
     */
    public String getTrack() {
        return mTrack;
    }

    /**
     *
     * @param num
     */
    public void setTrack(String track) {
        String oldTrack = this.mTrack;
        this.mTrack = track;
        propertyChangeSupport.firePropertyChange(Property.TRACK.name(), oldTrack, track);
    }
    
    public Integer getTrackLength() {
        return mTrackLength;
    }
    
    public void setTrackLength(Integer trackLength) {
        this.mTrackLength = trackLength;
    }
    
    public Integer getYear() {
        return mYear;
    }
    
    public void setYear(Integer year) {
        Integer oldYear = this.mYear;
        this.mYear = year;
        propertyChangeSupport.firePropertyChange(Property.YEAR.name(), oldYear, year);
        
    }

    /**
     *
     * @return
     */
    public List<Comment> getComments() {
        return Collections.unmodifiableList(mComments);
    }

    /**
     *
     * @param comments
     */
    public void setComments(List<Comment> comments) {
        this.mComments = comments;
    }

    /**
     *
     * @return
     */
    public Set<Score> getScores() {
        return Collections.unmodifiableSet(mScores);
    }

    /**
     *
     * @param scores
     */
    public void setScores(Set<Score> scores) {
        this.mScores = scores;
    }

    /**
     *
     * @return
     */
    public Set<String> getTags() {
        return Collections.unmodifiableSet(mTags);
    }

    /**
     *
     * @param tags
     */
    public void setTags(Set<String> tags) {
        this.mTags = tags;
    }

    /**
     *
     * @return
     */
    public Boolean getMayReadInfo() {
        return mMayReadInfo;
    }

    /**
     *
     * @param mayReadInfo
     */
    public void setMayReadInfo(Boolean mayReadInfo) {
        this.mMayReadInfo = mayReadInfo;
    }

    /**
     *
     * @return
     */
    public Boolean getMayListen() {
        return mMayListen;
    }

    /**
     *
     * @param mayListen
     */
    public void setMayListen(Boolean mayListen) {
        this.mMayListen = mayListen;
    }

    /**
     *
     * @return
     */
    public Boolean getMayCommentAndNote() {
        return mMayCommentAndNote;
    }

    /**
     *
     * @param mayCommentAndNote
     */
    public void setMayCommentAndNote(Boolean mayCommentAndNote) {
        this.mMayCommentAndNote = mayCommentAndNote;
    }

    /**
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     *
     * @return
     */
    public int getMusicHash() {
        return mFile != null ? mFile.hashCode() : 0;
    }
    
    public void addTag(String tag) {
        if (mTags.add(tag)) {
            propertyChangeSupport.firePropertyChange(Property.TAGS.name(), null, tag);
        }
    }
    
    public void removeTag(String tag) {
        if (mTags.remove(tag)) {
            propertyChangeSupport.firePropertyChange(Property.TAGS.name(), tag, null);
        }
    }
    
    public void setCommentAuthor(Integer index, String authorName) {
        Comment comment = mComments.get(index);
        comment.setAuthorName(authorName);
    }

    /**
     *
     */
    public enum Property {

        /**
         *
         */
        FILENAME,
        /**
         *
         */
        LOCAL,
        /**
         *
         */
        TITLE,
        /**
         *
         */
        ARTIST,
        /**
         *
         */
        ALBUM,
        /**
         *
         */
        TRACK,
        /**
         *
         */
        FILE_MISSING,
        /**
         *
         */
        YEAR,
        /**
         *
         */
        TAGS
    }
    
    @Override
    public Music clone() {
        return new Music(this);
    }
}