package org.geogebra.common.geogebra3D.euclidian3D.draw;

import org.geogebra.common.euclidian.plot.CurvePlotter;
import org.geogebra.common.geogebra3D.euclidian3D.EuclidianView3D;
import org.geogebra.common.geogebra3D.euclidian3D.openGL.PlotterBrush;
import org.geogebra.common.geogebra3D.euclidian3D.openGL.Renderer;
import org.geogebra.common.kernel.Matrix.CoordSys;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoLocusND;

/**
 * @author mathieu
 * 
 *         Drawable for locus
 * 
 */
public class DrawLocus3D extends Drawable3DCurves {

	private GeoLocusND locus;
	private CoordSys coordSys;

	/**
	 * @param a_view3d
	 *            the 3D view where the curve is drawn
	 * @param locus
	 *            the locus to draw
	 * @param geo
	 *            locus itself or parent geo
	 */
	public DrawLocus3D(EuclidianView3D a_view3d, GeoLocusND locus,
			GeoElement geo, CoordSys sys) {
		super(a_view3d, geo);
		this.locus = locus;
		this.coordSys = sys;

	}

	@Override
	public void drawGeometry(Renderer renderer) {

		renderer.getGeometryManager().draw(getGeometryIndex());

	}

	@Override
	protected boolean updateForItSelf() {

		EuclidianView3D view = getView3D();

		Renderer renderer = view.getRenderer();

		PlotterBrush brush = renderer.getGeometryManager().getBrush();
		brush.start(getReusableGeometryIndex());
		brush.setThickness(getGeoElement().getLineThickness(),
				(float) view.getScale());
		brush.setAffineTexture(0f, 0f);
		brush.setLength(1f);

		CurvePlotter.draw(brush, getLocus().getPoints(), coordSys);

		setGeometryIndex(brush.end());

		return true;

	}

	private GeoLocusND getLocus() {
		return locus;
	}

	@Override
	protected void updateForView() {
		if (getView3D().viewChangedByZoom()
				|| getView3D().viewChangedByTranslate()) {
			setWaitForUpdate();
		}
	}

	@Override
	public int getPickOrder() {
		return DRAW_PICK_ORDER_PATH;
	}

	@Override
	public void addToDrawable3DLists(Drawable3DLists lists) {
		addToDrawable3DLists(lists, DRAW_TYPE_CLIPPED_CURVES);
	}

	@Override
	public void removeFromDrawable3DLists(Drawable3DLists lists) {
		removeFromDrawable3DLists(lists, DRAW_TYPE_CLIPPED_CURVES);
	}
	
	@Override
	protected boolean isLabelVisible() {
		return false;
	}

}
