package org.geogebra.web.tablet;

import org.geogebra.common.GeoGebraConstants.Versions;
import org.geogebra.common.main.App;
import org.geogebra.common.move.ggtapi.models.Material;
import org.geogebra.web.html5.gui.tooltip.ToolTipManagerW;
import org.geogebra.web.html5.main.AppW;
import org.geogebra.web.tablet.gui.browser.TabletMaterialElement;
import org.geogebra.web.web.gui.browser.MaterialListElement;
import org.geogebra.web.web.gui.browser.SignInButton;
import org.geogebra.web.web.gui.browser.TabletSignInButton;
import org.geogebra.web.web.gui.laf.GLookAndFeel;

/**
 * Look and feel for tablet apps
 */
public class TabletLookAndFeel extends GLookAndFeel {

	/**
	 * Creates tablet LAF
	 */
	public TabletLookAndFeel() {
		ToolTipManagerW.setEnabled(false);
	}
	@Override
	public void addWindowClosingHandler(AppW app) {
		// no close message on tablet
	}

	@Override
	public void removeWindowClosingHandler() {
		// no close message on tablet
	}

	@Override
	public String getType() {
		return "tablet";
	}

	@Override
	public boolean isTablet() {
		return true;
	}

	@Override
	public boolean copyToClipboardSupported() {
		return true;
	}

	@Override
	public SignInButton getSignInButton(final App app) {
		return new TabletSignInButton(app);
	}

	@Override
	public MaterialListElement getMaterialElement(final Material m,
	        final AppW app, boolean isLocal) {
		return new TabletMaterialElement(m, app, isLocal);
	}

	@Override
	public boolean exportSupported() {
		return true;
	}

	@Override
	public boolean externalDriveSupported() {
		return false;
	}

	@Override
	public boolean supportsGoogleDrive() {
		return false;
	}

	/**
	 * @return style name for app frame
	 */
	public String getFrameStyleName() {
		return "Tablet";
	}

	@Override
	public boolean examSupported(boolean tablet) {
		return tablet;
	}

	@Override
	public boolean printSupported() {
		return false;
	}

	@Override
	public Versions getVersion(int dim) {
		return Versions.ANDROID_WEBVIEW;
	}

	@Override
	public boolean supportsFullscreen() {
		return false;
	}

}
