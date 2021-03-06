package fr.utc.lo23.sharutc.ui;

import com.google.inject.Inject;
import fr.utc.lo23.sharutc.controler.command.search.MusicSearchCommand;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.domain.Music;
import fr.utc.lo23.sharutc.model.domain.SearchCriteria;
import fr.utc.lo23.sharutc.model.userdata.ActivePeerList;
import fr.utc.lo23.sharutc.model.userdata.UserInfo;
import fr.utc.lo23.sharutc.ui.custom.card.SimpleCard;
import fr.utc.lo23.sharutc.ui.custom.CardList;
import fr.utc.lo23.sharutc.ui.custom.card.AlbumCard;
import fr.utc.lo23.sharutc.ui.custom.card.ArtistCard;
import fr.utc.lo23.sharutc.ui.custom.card.SongCard;
import fr.utc.lo23.sharutc.ui.custom.card.TagCard;
import fr.utc.lo23.sharutc.ui.custom.card.UserCard;
import fr.utc.lo23.sharutc.util.CollectionChangeListener;
import fr.utc.lo23.sharutc.util.CollectionEvent;
import static fr.utc.lo23.sharutc.util.CollectionEvent.Type.ADD;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchResultController extends SongSelectorController implements
        RighpaneInterface, CollectionChangeListener, Initializable,
        AlbumCard.IAlbumCard, UserCard.IUserCard, ArtistCard.IArtistCard,
        TagCard.ITagCard {

    private static final Logger log = LoggerFactory
            .getLogger(SearchResultController.class);
    @FXML
    public VBox scrollPaneContent;
    private String mCurrentCriteriaSearch;
    private CardList mSongList;
    private CardList mFriendList;
    private CardList mArtistList;
    private CardList mAlbumList;
    private CardList mTagList;
    private List<String> mArtistNameFound;
    private List<String> mAlbumNameFound;
    private HashMap<String, TagCard> mTagFound;
    private ISearchResultController mInterface;
    @Inject
    private AppModel mAppModel;
    @Inject
    private MusicSearchCommand mMusicSearchCommand;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //listen for changes on the AppModel
        mAppModel.getSearchResults().addPropertyChangeListener(this);

        mSongList = new CardList("Songs", "bgBlue");
        mFriendList = new CardList("People", "bgGreen");
        mArtistList = new CardList("Artists", "bgRed");
        mAlbumList = new CardList("Albums", "bgOrange");
        mTagList = new CardList("Tags", "bgBlue");

        mArtistNameFound = new ArrayList<String>();
        mAlbumNameFound = new ArrayList<String>();
        mTagFound = new HashMap<String, TagCard>();

        scrollPaneContent.getChildren().addAll(mSongList,
                mFriendList, mArtistList, mAlbumList, mTagList);

    }

    public void searchAll(final String criteriaString) {
        log.debug("search all -> " + criteriaString);
        clearPreviousSearch();
        mCurrentCriteriaSearch = criteriaString.toLowerCase();
        searchMusic(mCurrentCriteriaSearch);
        searchPeople(mCurrentCriteriaSearch);
    }

    private void searchMusic(final String criteriaString) {
        //execute an asynchronous search
        final Task<Void> searchMusicTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                log.debug("search music -> " + criteriaString);
                SearchCriteria critera = new SearchCriteria(criteriaString);
                mMusicSearchCommand.setSearchCriteria(critera);
                mMusicSearchCommand.execute();
                return null;
            }
        };
        new Thread(searchMusicTask, "Search Music").start();
    }

    private void searchPeople(final String criteriaString) {
        log.debug("search people -> " + criteriaString);
        ActivePeerList peers = mAppModel.getActivePeerList();
        final HashMap<UserInfo, Date> userInfoList = peers.getActivePeers();
        for (UserInfo userInfo : userInfoList.keySet()) {
            log.debug("peer " + userInfo.getFirstName() + " " + userInfo.getLastName() + " " + userInfo.getLogin());
            if (userInfo.getFirstName().toLowerCase().contains(criteriaString)
                    || userInfo.getLastName().toLowerCase().contains(criteriaString)
                    || userInfo.getLogin().toLowerCase().contains(criteriaString)
                    || criteriaString.contains(userInfo.getFirstName().toLowerCase())
                    || criteriaString.contains(userInfo.getLastName().toLowerCase())
                    || criteriaString.contains(userInfo.getLogin().toLowerCase())) {
                addChild(new UserCard(userInfo, this));
            }
        }
    }

    private void clearPreviousSearch() {
        mArtistNameFound.clear();
        mAlbumNameFound.clear();
        mSongList.clear();
        mArtistList.clear();
        mAlbumList.clear();
        mFriendList.clear();
    }

    public void setInterface(ISearchResultController i) {
        super.setInterface(i);
        this.mInterface = i;
    }

    public void addChild(SimpleCard card) {
        if (card instanceof UserCard) {
            mFriendList.addChild(card);
        } else if (card instanceof SongCard) {
            mSongList.addChild(card);
        } else if (card instanceof ArtistCard) {
            mArtistList.addChild(card);
        } else if (card instanceof AlbumCard) {
            mAlbumList.addChild(card);
        } else if (card instanceof TagCard) {
            mTagList.addChild(card);
        }
    }

    private void addChildFromOtherThread(final SimpleCard card) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                addChild(card);
            }
        });
    }

    private void increaseTagCardWeight(final TagCard tagCard) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tagCard.increaseTagWeight();
            }
        });
    }

    @Override
    public void onPeopleDetailsRequested(UserInfo userInfo) {
        mInterface.onPeopleDetailRequested(userInfo);
    }

    @Override
    public void onArtistDetailRequested(String artistName, SongDetailController.CatalogType type) {
        mInterface.onArtistDetailRequested(artistName, type);
    }

    @Override
    public void onAlbumDetailRequested(String albumName, SongDetailController.CatalogType type) {
        mInterface.onAlbumDetailRequested(albumName, type);
    }

    @Override
    public void collectionChanged(CollectionEvent ev) {
        //Since the MusicSearchCommand is executed on another Thread
        //this method will not be called from the UI Thread.
        //You can't directly update the UI from there.
        log.debug("collectionChanged -> " + ev.getType().name());
        switch (ev.getType()) {
            case ADD:
                Music m = ((Music) ev.getItem());
                log.debug("add music " + m.getTitle() + " " + m.getArtist() + " " + m.getAlbum());
                if (m.getAlbum().toLowerCase().contains(mCurrentCriteriaSearch) && !mAlbumNameFound.contains(m.getAlbum())) {
                    log.debug("add music -- album");
                    mAlbumNameFound.add(m.getAlbum());

                    AlbumCard card = new AlbumCard(m, SearchResultController.this);
                    if (!(mAppModel.getProfile().getUserInfo().getPeerId() == m.getOwnerPeerId())) {
                        card.setCatalogType(SongDetailController.CatalogType.search);
                    }

                    addChildFromOtherThread(card);
                }
                if (m.getArtist().toLowerCase().contains(mCurrentCriteriaSearch) && !mArtistNameFound.contains(m.getArtist())) {
                    log.debug("add music -- artist");
                    mArtistNameFound.add(m.getArtist());
                    ArtistCard card = new ArtistCard(m.getArtist(), SearchResultController.this);
                    if (!(mAppModel.getProfile().getUserInfo().getPeerId() == m.getOwnerPeerId())) {
                        card.setCatalogType(SongDetailController.CatalogType.search);
                    }
                    addChildFromOtherThread(card);
                }
                if (m.getTitle().toLowerCase().contains(mCurrentCriteriaSearch)) {
                    log.debug("add music -- song");
                    addChildFromOtherThread(new SongCard(m, SearchResultController.this, mAppModel));
                }
                final Set<String> tags = m.getTags();
                for (final String tag : tags) {
                    if (!mTagFound.containsKey(tag)) {
                        final TagCard newTagCard = new TagCard(tag, this);
                        newTagCard.setDragEnable(false);
                        mTagFound.put(tag, newTagCard);
                        addChildFromOtherThread(newTagCard);
                    } else {
                        increaseTagCardWeight(mTagFound.get(tag));
                    }
                }
                break;
        }

    }

    @Override
    public void onDetach() {
        mAppModel.getSearchResults().removePropertyChangeListener(this);
    }

    @Override
    public void onTagSelected(String tagName) {
        mInterface.onTagFilterRequested(tagName, SongDetailController.CatalogType.search);
    }

    @Override
    public void onMusicDropOnTag(String tagName) {
        //Do nothing.
    }

    public interface ISearchResultController extends SongListController.ISongListController {

        /**
         * display user details
         *
         * @param user
         */
        void onPeopleDetailRequested(UserInfo user);

        /**
         * display user details
         *
         * @param music
         */
        public void onArtistDetailRequested(String artistName, SongDetailController.CatalogType type);

        /**
         * display album details
         *
         * @param music
         */
        public void onAlbumDetailRequested(String albumName, SongDetailController.CatalogType type);

        /**
         * The {@link ISongDetailController} is being asked to show the local
         * catalog filtered with the tag.
         *
         * @param tagName the tag used to filter the local catalog.
         */
        public void onTagFilterRequested(String tagName, SongDetailController.CatalogType type);
    }
}
