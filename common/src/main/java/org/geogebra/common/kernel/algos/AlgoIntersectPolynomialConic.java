/* 
GeoGebra - Dynamic Mathematics for Everyone
http://www.geogebra.org

This file is part of GeoGebra.

This program is free software; you can redistribute it and/or modify it 
under the terms of the GNU General Public License as published by 
the Free Software Foundation.

 */

/*
 * AlgoIntersectPolynomialConic.java
 *
 * Created on 14.07.2010, 14:28
 */

package org.geogebra.common.kernel.algos;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.analysis.polynomials.PolynomialFunction;
import org.geogebra.common.euclidian.EuclidianConstants;
import org.geogebra.common.kernel.Construction;
import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.Matrix.Coords;
import org.geogebra.common.kernel.arithmetic.PolyFunction;
import org.geogebra.common.kernel.commands.Commands;
import org.geogebra.common.kernel.geos.GeoConic;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.geos.GeoNumeric;
import org.geogebra.common.kernel.geos.GeoPoint;
import org.geogebra.common.kernel.implicit.GeoImplicit;
import org.geogebra.common.kernel.kernelND.GeoConicNDConstants;

/**
 * Intersect polynomial function with a conic
 */
public class AlgoIntersectPolynomialConic extends AlgoSimpleRootsPolynomial {

	private GeoFunction h; // input
	private GeoConic c;



	/**
	 * 
	 * @param cons
	 *            construction
	 * @param h
	 *            polynomial function
	 * @param c
	 *            conic
	 */
	public AlgoIntersectPolynomialConic(Construction cons, GeoFunction h,
			GeoConic c) {
		super(cons, h, c);
		this.h = h;
		this.c = c;

		// initForNearToRelationship();
		compute();
	}

	/**
	 * @param cons
	 *            construction
	 * @param labels
	 *            labels for result
	 * @param setLabel
	 *            whether to set labels
	 * @param h
	 *            function
	 * @param c
	 *            conic
	 */
	public AlgoIntersectPolynomialConic(Construction cons, String[] labels,
			boolean setLabel, GeoFunction h, GeoConic c) {
		super(cons, h, c);
		this.h = h;
		this.c = c;
		compute();

		addIncidence();
	}

	/**
	 * @author Tam
	 * 
	 *         for special cases of e.g. AlgoIntersectLineConic
	 */
	private void addIncidence() {
		// TODO
	}

	@Override
	public void compute() {

		double[] A = c.getFlatMatrix();
		PolyFunction pf = null;
		if (h.isPolynomialFunction(false)) {
			pf = h.getFunction().getNumericPolynomialDerivative(0,
					false);

		}
		if (pf != null) {
			PolynomialFunction y = new PolynomialFunction(pf.getCoeffs());
			PolynomialFunction r = new PolynomialFunction(new double[] { A[2],
					2 * A[4], A[0] });
			r = r.add(y.multiply(new PolynomialFunction(new double[] {
					2 * A[5], 2 * A[3] })));
			r = r.add(y.multiply(y.multiply(new PolynomialFunction(
					new double[] { A[1] }))));
			// Application.debug("r = "+r.toString());
			setRootsPolynomial(r);
		} else {
			Kernel ker = cons.getKernel();
			boolean oldSilentMode = ker.isSilentMode();
			ker.setSilentMode(true);

			/*
			 * try { substituteFunctionX = (GeoFunction)
			 * ker.getAlgebraProcessor().processValidExpression(
			 * ker.getParser().parseGeoGebraExpression("x"))[0]; } catch
			 * (MyError e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } catch (ParseException e) { // TODO
			 * Auto-generated catch block e.printStackTrace(); } catch
			 * (Exception e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */

			GeoImplicit iPoly = kernel.newImplicitPoly(cons);
			iPoly.preventPathCreation();
			c.toGeoImplicitCurve(iPoly);

			GeoFunction paramEquation = new GeoFunction(cons, iPoly, null, h);

			// int nroots = 0;
			// double res[] = new double[2];

			// bounds of the ellipse:
			// axx+byy+c+2dxy+2ex+2fy = b yy + (2dx + 2f)+(axx+2ex+c) has
			// exactly one root wrt y if
			// 0=(2dx +2f)^2-4*b(axx+2ex+c)=4*((d^2-ab)xx+(2df-2eb)x+(f^2-bc))

			// nroots = kernel.getEquationSolver().solveQuadratic(
			// new double[] { -A[5] * A[5] + A[1] * A[2],
			// 2 * (A[1] * A[4] - A[3] * A[5]),
			// A[0] * A[1] - A[3] * A[3] }, res,
			// Kernel.STANDARD_PRECISION);

			Coords midPoint = null;
			double[] halfAxes = null;
			double ellipseCirlcleFactor = 1.05;
			double hyperParaBolaFactor = 0.5;

			// bounds of the ellipse:
			// axx+byy+c+2dxy+2ex+2fy = b yy + (2dx + 2f)+(axx+2ex+c) has
			// exactly one root wrt y if
			// 0=(2dx +2f)^2-4*b(axx+2ex+c)=4*((d^2-ab)xx+(2df-2eb)x+(f^2-bc))
			if (c.getType() == GeoConicNDConstants.CONIC_CIRCLE
					|| c.getType() == GeoConicNDConstants.CONIC_ELLIPSE) {

				midPoint = c.getMidpoint();
				halfAxes = c.getHalfAxes();
			}

			AlgoRoots algo = null;
			if (midPoint != null && halfAxes != null) {
				algo = new AlgoRoots(cons, paramEquation, new GeoNumeric(cons,
						midPoint.getX() - ellipseCirlcleFactor
								* Math.max(halfAxes[0], halfAxes[1])),
						new GeoNumeric(cons, midPoint.getX()
								+ ellipseCirlcleFactor
								* Math.max(halfAxes[0], halfAxes[1])));

				// Log.debug("interval(ellipse/circle): "
				// + (midPoint.getX() - ellipseCirlcleFactor
				// * Math.max(halfAxes[0], halfAxes[1]))
				// + ", "
				// + (midPoint.getX() + ellipseCirlcleFactor
				// * Math.max(halfAxes[0], halfAxes[1])));
			} else {
				algo = new AlgoRoots(cons, paramEquation, new GeoNumeric(cons,
						h.getMinParameter() - hyperParaBolaFactor
								* Math.abs(h.getMinParameter())),
						new GeoNumeric(cons, h.getMaxParameter()
								+ hyperParaBolaFactor
								* Math.abs(h.getMaxParameter())));

				// Log.debug("interval(hyperbola/parabola): "
				// + (h.getMinParameter() - Math.abs(h.getMinParameter()
				// * hyperParaBolaFactor))
				// + ", "
				// + (h.getMaxParameter() + Math.abs(h.getMaxParameter()
				// * hyperParaBolaFactor)));
			}

			GeoPoint[] rootPoints = algo.getRootPoints();
			List<double[]> valPairs = new ArrayList<double[]>();
			for (int i = 0; i < rootPoints.length; i++) {
				double t = rootPoints[i].getX();
				valPairs.add(new double[] { t, h.evaluate(t) });
			}

			ker.setSilentMode(oldSilentMode);

			setPoints(valPairs);
			return;

		}

	}

	@Override
	public Commands getClassName() {
		return Commands.Intersect;
	}

	@Override
	public int getRelatedModeID() {
		return EuclidianConstants.MODE_INTERSECT;
	}

	@Override
	protected double getYValue(double x) {
		return h.evaluate(x);
	}

}
