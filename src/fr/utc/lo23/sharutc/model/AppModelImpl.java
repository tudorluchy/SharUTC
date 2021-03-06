package fr.utc.lo23.sharutc.model;

import fr.utc.lo23.sharutc.model.domain.Catalog;
import fr.utc.lo23.sharutc.model.domain.RightsList;
import fr.utc.lo23.sharutc.model.domain.TagMap;
import fr.utc.lo23.sharutc.model.userdata.ActivePeerList;
import fr.utc.lo23.sharutc.model.userdata.Profile;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@inheritDoc}
 */
@Singleton
public class AppModelImpl implements AppModel, Serializable {

    private static final long serialVersionUID = -8328268947100193264L;
    private static final Logger log = LoggerFactory
            .getLogger(AppModelImpl.class);
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private ErrorBus errorBus = new ErrorBus();
    private ActivePeerList activePeerList = new ActivePeerList();
    private TagMap networkTagMap = new TagMap();
    private Catalog tmpCatalog = new Catalog();
    private Catalog remoteUserCatalog = new Catalog();
    private Catalog searchResults = new Catalog();
    protected Long currentConversationId = new Long(0);
    private Profile profile;
    private Catalog localCatalog;
    private RightsList rightsList;

    /**
     * Empty constructor, no real use in application, created by injection
     */
    public AppModelImpl() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getCurrentConversationId() {
        return currentConversationId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getNextConversationId() {
        return ++currentConversationId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActivePeerList getActivePeerList() {
        return activePeerList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setActivePeerList(ActivePeerList activePeerList) {
        if (activePeerList != null) {
            log.trace("Setting ACTIVE peers list (contains {} peer{})", activePeerList.size(), activePeerList.size() > 1 ? "s" : "");
        } else {
            log.trace("Setting ACTIVE peers list (null)");
        }
        ActivePeerList oldActivePeerList = this.activePeerList;
        this.activePeerList = activePeerList;
        propertyChangeSupport.firePropertyChange(
                Property.ACTIVE_PEERS.name(), oldActivePeerList,
                activePeerList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Profile getProfile() {
        return profile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProfile(Profile profile) {
        log.debug("Setting profile");
        Profile oldLoadedProfile = this.profile;
        this.profile = profile;
        this.propertyChangeSupport.firePropertyChange(
                Property.PROFILE.name(), oldLoadedProfile,
                profile);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TagMap getNetworkTagMap() {
        return networkTagMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNetworkTagMap(TagMap networkTagMap) {
        log.debug("Setting networkTagMap");
        TagMap oldNetworkTagMap = this.networkTagMap;
        this.networkTagMap = networkTagMap;
        propertyChangeSupport.firePropertyChange(
                Property.NETWORK_TAG_MAP.name(), oldNetworkTagMap,
                networkTagMap);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ErrorBus getErrorBus() {
        return errorBus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setErrorBus(ErrorBus errorBus) {
        this.errorBus = errorBus;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getLocalCatalog() {
        return localCatalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocalCatalog(Catalog localCatalog) {
        this.localCatalog = localCatalog;
        propertyChangeSupport.firePropertyChange(Property.LOCAL_CATALOG.name(),
                null, this.localCatalog);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getTmpCatalog() {
        return tmpCatalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTmpCatalog(Catalog tmpCatalog) {
        this.tmpCatalog = tmpCatalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getSearchResults() {
        return searchResults;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSearchResults(Catalog searchResults) {
        this.searchResults = searchResults;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getRemoteUserCatalog() {
        return remoteUserCatalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRemoteUserCatalog(Catalog remoteUserCatalog) {
        this.remoteUserCatalog = remoteUserCatalog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RightsList getRightsList() {
        return rightsList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setRightsList(RightsList rightsList) {
        this.rightsList = rightsList;
    }

    /**
     *
     */
    public enum Property {

        /**
         *
         */
        ACTIVE_PEERS,
        /**
         *
         */
        PROFILE,
        /**
         *
         */
        NETWORK_TAG_MAP,
        /**
         *
         */
        LOCAL_CATALOG
    }
}
