package org.geogebra.common.gui.dialog.options.model;

import org.geogebra.common.kernel.geos.GeoSegment;
import org.geogebra.common.main.App;

public class DecoSegmentModel extends NumberOptionsModel {

	private IComboListener listener;
	
	public DecoSegmentModel(App app) {
		super(app);
	}
	
	private GeoSegment getGeoSegmentAt(int index) {
		return (GeoSegment)getObjectAt(index);
	}

	public void setListener(IComboListener listener) {
		this.listener = listener;
	}

	@Override
	public void updateProperties() {
	
		GeoSegment geo0 = getGeoSegmentAt(0);
		listener.setSelectedIndex(geo0.getDecorationType());
		
	}

	
	@Override
	public boolean isValidAt(int index) {
		return (getObjectAt(index) instanceof GeoSegment);
	}

	@Override
	protected void apply(int index, int value) {
		GeoSegment geo = getGeoSegmentAt(index);
		geo.setDecorationType(value);
		geo.updateRepaint();
		}

	@Override
	protected int getValueAt(int index) {
		return getGeoSegmentAt(index).getDecorationType();
	}
	
	public static int getDecoTypeLength() {
		return GeoSegment.getDecoTypes().length;
	}

	@Override
	public PropertyListener getListener() {
		return listener;
	};
}
