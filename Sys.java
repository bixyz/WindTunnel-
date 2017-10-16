
/*
                               Sys

                           A Java Applet
               to design a  Kutta-Joukowski Airfoil and
                  then wind tunnel test the design
                  then post-process the results
                          Derived from FoilSim II
                   Derived from 3 TunnelSys applications
           This applet is used to "advertise" for the applications

                     Version 1.0e   - 6 Aug 09

                         Written by Tom Benson
                          and Anthony Vila
                       NASA Glenn Research Center

>                              NOTICE
>This software is in the Public Domain.  It may be freely copied and used in
>non-commercial products, assuming proper credit to the author is given.  IT
>MAY NOT BE RESOLD.  If you want to use the software for commercial
>products, contact the author.
>No copyright is claimed in the United States under Title 17, U. S. Code.
>This software is provided "as is" without any warranty of any kind, either
>express, implied, or statutory, including, but not limited to, any warranty
>that the software will conform to specifications, any implied warranties of
>merchantability, fitness for a particular purpose, and freedom from
>infringement, and any warranty that the documentation will conform to the
>program, or any warranty that the software will be error free.
>In no event shall NASA be liable for any damages, including, but not
>limited to direct, indirect, special or consequential damages, arising out
>of, resulting from, or in any way connected with this software, whether or
>not based on warranty, contract, tort or otherwise, whether or not injury
>was sustained by persons or property or otherwise, and whether or not loss
>was sustained from, or arose out of the results of, or use of, the software
>or services provided hereunder.

  New test -
               - break FoilSim II into three programs
                   passing data by files
                 this is a combination of the three application programs
               * add design
               * add tunnel
               *   write data
               * add process
                 re-size to fit smaller window for the protal

                                           TJB  6 Aug 09
*/

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Scrollbar;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// TODO: Auto-generated Javadoc
/** The Class Sys. */
public class Sys extends java.applet.Applet {

	/** The Class Pan. */
	class MainPanel extends Panel implements ComponentListener {

		/** The Class Dsys. */
		class DesignCard extends Panel {

			/** The Class Ind. */
			class Ind extends Panel {

				/** The Class Viewer. */
				class DesignViewer extends Canvas implements Runnable {

					/** The Constant serialVersionUID. */
					private static final long serialVersionUID = 1L;

					/** The anchor. */
					private Point locate;

					/** The anchor. */
					private Point anchor;

					/** The runner. */
					private Thread runner;

					/**
					 * Instantiates a new viewer.
					 *
					 * @param target
					 *            the target
					 */
					DesignViewer(final Sys target) {
						this.setBackground(Color.black);
						this.runner = null;
					}

					/**
					 * Handle.
					 *
					 * @param x
					 *            the x
					 * @param y
					 *            the y
					 */
					public void handle(final int x, final int y) {
						// determine location
						if (y >= 30) {
							if (x >= 30) { // translate
								if (DesignCard.this.displ != 2) {
									this.locate = new Point(x, y);
									DesignCard.this.yt = DesignCard.this.yt
											+ (int) (.2 * (this.locate.y - this.anchor.y));
									DesignCard.this.xt = DesignCard.this.xt
											+ (int) (.4 * (this.locate.x - this.anchor.x));
									DesignCard.this.xt = limit(-280, 320, DesignCard.this.xt);
									DesignCard.this.yt = limit(-300, 300, DesignCard.this.yt);

									DesignCard.this.xt1 = DesignCard.this.xt + DesignCard.this.spanfac;
									DesignCard.this.yt1 = DesignCard.this.yt - DesignCard.this.spanfac;
									DesignCard.this.xt2 = DesignCard.this.xt - DesignCard.this.spanfac;
									DesignCard.this.yt2 = DesignCard.this.yt + DesignCard.this.spanfac;
								}
								if (DesignCard.this.displ == 2) { // move the rake
									this.locate = new Point(x, y);
									DesignCard.this.xflow = DesignCard.this.xflow
											+ .01 * (this.locate.x - this.anchor.x);
									DesignCard.this.xflow = limit(-10.0, 0.0, DesignCard.this.xflow);

									DesignCard.this.computeFlow();
								}
							}
							if (x < 30) { // zoom widget
								DesignCard.this.sldloc = y;
								DesignCard.this.sldloc = limit(130, 265, DesignCard.this.sldloc);

								DesignCard.this.fact = 5.0 + (DesignCard.this.sldloc - 130) * 1.0;
								DesignCard.this.spanfac = (int) (2.0 * DesignCard.this.fact * DesignCard.this.aspr
										* .3535);
								DesignCard.this.xt1 = DesignCard.this.xt + DesignCard.this.spanfac;
								DesignCard.this.yt1 = DesignCard.this.yt - DesignCard.this.spanfac;
								DesignCard.this.xt2 = DesignCard.this.xt - DesignCard.this.spanfac;
								DesignCard.this.yt2 = DesignCard.this.yt + DesignCard.this.spanfac;
							}
						}
					}

					/**
					 * Handleb.
					 *
					 * @param x
					 *            the x
					 * @param y
					 *            the y
					 */
					public void handleMouseUp(final int x, final int y) {
						if (y >= 100 && y <= 120) {
							if (x >= 0 && x <= 40) { // find
								DesignCard.this.xt = 200;
								DesignCard.this.yt = 165;
								DesignCard.this.fact = 15.0;
								DesignCard.this.sldloc = 140;
								DesignCard.this.spanfac = (int) (2.0 * DesignCard.this.fact * DesignCard.this.aspr
										* .3535);
								DesignCard.this.xt1 = DesignCard.this.xt + DesignCard.this.spanfac;
								DesignCard.this.yt1 = DesignCard.this.yt - DesignCard.this.spanfac;
								DesignCard.this.xt2 = DesignCard.this.xt - DesignCard.this.spanfac;
								DesignCard.this.yt2 = DesignCard.this.yt + DesignCard.this.spanfac;
							}
						}
						Ind.this.view.repaint();
					}

					/**
					 * Insets.
					 *
					 * @return the insets
					 */
					public Insets insets() {
						return new Insets(0, 10, 0, 10);
					}

					/**
					 * (non-Javadoc).
					 *
					 * @param evt
					 *            the evt
					 * @param x
					 *            the x
					 * @param y
					 *            the y
					 * @return true, if successful
					 * @see java.awt.Component#mouseDown(java.awt.Event, int, int)
					 */
					@Override
					public boolean mouseDown(final Event evt, final int x, final int y) {
						this.anchor = new Point(x, y);
						return true;
					}

					/**
					 * (non-Javadoc).
					 *
					 * @param evt
					 *            the evt
					 * @param x
					 *            the x
					 * @param y
					 *            the y
					 * @return true, if successful
					 * @see java.awt.Component#mouseDrag(java.awt.Event, int, int)
					 */
					@Override
					public boolean mouseDrag(final Event evt, final int x, final int y) {
						this.handle(x, y);
						return true;
					}

					/**
					 * (non-Javadoc).
					 *
					 * @param evt
					 *            the evt
					 * @param x
					 *            the x
					 * @param y
					 *            the y
					 * @return true, if successful
					 * @see java.awt.Component#mouseUp(java.awt.Event, int, int)
					 */
					@Override
					public boolean mouseUp(final Event evt, final int x, final int y) {
						this.handleMouseUp(x, y);
						return true;
					}

					/**
					 * (non-Javadoc).
					 *
					 * @param g
					 *            the g
					 * @see java.awt.Canvas#paint(java.awt.Graphics)
					 */
					@Override
					public void paint(final Graphics g) {
						if (Sys.this.mainPannel.designCard.isVisible()) {
							final int exes[] = new int[8];
							final int whys[] = new int[8];
							final int camx[] = new int[19];
							final int camy[] = new int[19];
							new Color(0, 0, 0);
							if (DesignCard.this.planet == PLANET_EARTH) {
							}
							if (DesignCard.this.planet == PLANET_MARS) {
							}
							if (DesignCard.this.planet == PLANET_WATER) {
							}
							if (DesignCard.this.planet >= PLANET_AIR_TEMP) {
							}
							Sys.this.designViewImgBuffGraphContext.setColor(Color.black);
							Sys.this.designViewImgBuffGraphContext.fillRect(0, 0, Sys.DESIGN_VIEWER_WIDTH,
									Sys.DESIGN_VIEWER_HEIGHT);

							this.paintWingSurface(exes, whys);

							this.paintFrontFoil(exes, whys, camx, camy);

							this.paintSpinCylinderBall(exes, whys);

							this.paintZoomIn();

							g.drawImage(Sys.this.designViewImageBuffer, 0, 0, this);
						}
					}

					/**
					 * Paint front foil.
					 *
					 * @param exes
					 *            the exes
					 * @param whys
					 *            the whys
					 * @param camx
					 *            the camx
					 * @param camy
					 *            the camy
					 */
					private void paintFrontFoil(final int[] exes, final int[] whys, final int[] camx,
							final int[] camy) {
						int i;
						// front foil
						Sys.this.designViewImgBuffGraphContext.setColor(Color.white);
						exes[1] = (int) (DesignCard.this.fact * DesignCard.this.xpl[0][DesignCard.this.npt2])
								+ DesignCard.this.xt2;
						whys[1] = (int) (DesignCard.this.fact * -DesignCard.this.ypl[0][DesignCard.this.npt2])
								+ DesignCard.this.yt2;
						exes[2] = (int) (DesignCard.this.fact * DesignCard.this.xpl[0][DesignCard.this.npt2])
								+ DesignCard.this.xt2;
						whys[2] = (int) (DesignCard.this.fact * -DesignCard.this.ypl[0][DesignCard.this.npt2])
								+ DesignCard.this.yt2;
						for (i = 1; i <= DesignCard.this.npt2 - 1; ++i) {
							exes[0] = exes[1];
							whys[0] = whys[1];
							exes[1] = (int) (DesignCard.this.fact * DesignCard.this.xpl[0][DesignCard.this.npt2 - i])
									+ DesignCard.this.xt2;
							whys[1] = (int) (DesignCard.this.fact * -DesignCard.this.ypl[0][DesignCard.this.npt2 - i])
									+ DesignCard.this.yt2;
							exes[3] = exes[2];
							whys[3] = whys[2];
							exes[2] = (int) (DesignCard.this.fact * DesignCard.this.xpl[0][DesignCard.this.npt2 + i])
									+ DesignCard.this.xt2;
							whys[2] = (int) (DesignCard.this.fact * -DesignCard.this.ypl[0][DesignCard.this.npt2 + i])
									+ DesignCard.this.yt2;
							camx[i] = (exes[1] + exes[2]) / 2;
							camy[i] = (whys[1] + whys[2]) / 2;
							Sys.this.designViewImgBuffGraphContext.fillPolygon(exes, whys, 4);
						}
					}

					/**
					 * Paint spin cylinder ball.
					 *
					 * @param exes
					 *            the exes
					 * @param whys
					 *            the whys
					 */
					private void paintSpinCylinderBall(final int[] exes, final int[] whys) {
						// spin the cylinder and ball
						if (DesignCard.this.foiltype >= FOILTYPE_CYLINDER) {
							exes[0] = (int) (DesignCard.this.fact * (.5
									* (DesignCard.this.xpl[0][1] + DesignCard.this.xpl[0][DesignCard.this.npt2])
									+ DesignCard.this.rval
											* Math.cos(DesignCard.this.convdr * (DesignCard.this.plthg[1] + 180.))))
									+ DesignCard.this.xt2;
							whys[0] = (int) (DesignCard.this.fact * (-DesignCard.this.ypl[0][1] + DesignCard.this.rval
									* Math.sin(DesignCard.this.convdr * (DesignCard.this.plthg[1] + 180.))))
									+ DesignCard.this.yt2;
							exes[1] = (int) (DesignCard.this.fact
									* (.5 * (DesignCard.this.xpl[0][1] + DesignCard.this.xpl[0][DesignCard.this.npt2])
											+ DesignCard.this.rval
													* Math.cos(DesignCard.this.convdr * DesignCard.this.plthg[1])))
									+ DesignCard.this.xt2;
							whys[1] = (int) (DesignCard.this.fact * (-DesignCard.this.ypl[0][1] + DesignCard.this.rval
									* Math.sin(DesignCard.this.convdr * DesignCard.this.plthg[1])))
									+ DesignCard.this.yt2;
							Sys.this.designViewImgBuffGraphContext.setColor(Color.red);
							Sys.this.designViewImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1], whys[1]);
						}
					}

					/**
					 * Paint wing surface.
					 *
					 * @param exes
					 *            the exes
					 * @param whys
					 *            the whys
					 */
					private void paintWingSurface(final int[] exes, final int[] whys) {
						int i;
						// wing surface
						Sys.this.designViewImgBuffGraphContext.setColor(Color.red);
						exes[1] = (int) (DesignCard.this.fact * DesignCard.this.xpl[0][DesignCard.this.npt2])
								+ DesignCard.this.xt1;
						whys[1] = (int) (DesignCard.this.fact * -DesignCard.this.ypl[0][DesignCard.this.npt2])
								+ DesignCard.this.yt1;
						exes[2] = (int) (DesignCard.this.fact * DesignCard.this.xpl[0][DesignCard.this.npt2])
								+ DesignCard.this.xt2;
						whys[2] = (int) (DesignCard.this.fact * -DesignCard.this.ypl[0][DesignCard.this.npt2])
								+ DesignCard.this.yt2;
						for (i = 1; i <= DesignCard.this.npt2 - 1; ++i) {
							exes[0] = exes[1];
							whys[0] = whys[1];
							exes[1] = (int) (DesignCard.this.fact * DesignCard.this.xpl[0][DesignCard.this.npt2 - i])
									+ DesignCard.this.xt1;
							whys[1] = (int) (DesignCard.this.fact * -DesignCard.this.ypl[0][DesignCard.this.npt2 - i])
									+ DesignCard.this.yt1;
							exes[3] = exes[2];
							whys[3] = whys[2];
							exes[2] = (int) (DesignCard.this.fact * DesignCard.this.xpl[0][DesignCard.this.npt2 - i])
									+ DesignCard.this.xt2;
							whys[2] = (int) (DesignCard.this.fact * -DesignCard.this.ypl[0][DesignCard.this.npt2 - i])
									+ DesignCard.this.yt2;
							Sys.this.designViewImgBuffGraphContext.fillPolygon(exes, whys, 4);
						}
					}

					/**
					 * Paint zoom in.
					 */
					private void paintZoomIn() {
						// zoom in
						Sys.this.designViewImgBuffGraphContext.setColor(Color.green);
						Sys.this.designViewImgBuffGraphContext.drawString("3D View", 10, 20);

						Sys.this.designViewImgBuffGraphContext.setColor(Color.black);
						Sys.this.designViewImgBuffGraphContext.fillRect(0, 130, 30, 150);

						Sys.this.designViewImgBuffGraphContext.setColor(Color.green);
						Sys.this.designViewImgBuffGraphContext.drawString("Zoom", 2, 280);
						Sys.this.designViewImgBuffGraphContext.drawLine(15, 135, 15, 265);

						Sys.this.designViewImgBuffGraphContext.fillRect(5, DesignCard.this.sldloc, 20, 5);

						Sys.this.designViewImgBuffGraphContext.setColor(Color.yellow);
						Sys.this.designViewImgBuffGraphContext.drawString("Find", 5, 110);
					}

					/**
					 * (non-Javadoc).
					 *
					 * @see java.lang.Runnable#run()
					 */
					@Override
					public void run() {
						int timer;

						timer = 100;
						while (true) {
							++DesignCard.this.antim;
							try {
								Thread.sleep(timer);
							} catch (final InterruptedException e) {
							}
							Ind.this.view.repaint();
							if (DesignCard.this.antim == 3) {
								DesignCard.this.antim = 0;
								DesignCard.this.ancol = -DesignCard.this.ancol; /* MODS 27 JUL 99 */
							}
							timer = 135 - (int) (.227 * DesignCard.this.vfsd / DesignCard.this.vconv);
							// make the ball spin
							if (DesignCard.this.foiltype >= FOILTYPE_CYLINDER) {
								DesignCard.this.plthg[1] = DesignCard.this.plthg[1]
										+ DesignCard.this.spin * DesignCard.this.spindr * 5.;
								if (DesignCard.this.plthg[1] < -360.0) {
									DesignCard.this.plthg[1] = DesignCard.this.plthg[1] + 360.0;
								}
								if (DesignCard.this.plthg[1] > 360.0) {
									DesignCard.this.plthg[1] = DesignCard.this.plthg[1] - 360.0;
								}
							}
						}
					}

					/** Start. */
					public void start() {
						if (this.runner == null) {
							this.runner = new Thread(this);
							this.runner.start();
						}
						DesignCard.this.antim = 0; /* MODS 21 JUL 99 */
						DesignCard.this.ancol = 1; /* MODS 27 JUL 99 */
						Sys.logger.info("Design Viewer Started");
					}

					/**
					 * (non-Javadoc).
					 *
					 * @param g
					 *            the g
					 * @see java.awt.Canvas#update(java.awt.Graphics)
					 */
					@Override
					public void update(final Graphics g) {
						Ind.this.view.paint(g);
					}
				} // end Viewer

				/** The Class In. */
				class In extends Panel {

					/** The Class Cyl. */
					class Cyl extends Panel {

						/** The Class Inl. */
						class Inl extends Panel {

							/** The Constant serialVersionUID. */
							private static final long serialVersionUID = 1L;

							/** The f 2. */
							TextField f1;
							
							/** The f 2. */
							TextField f2;

							/** The l 02. */
							Label l01;
							
							/** The l 02. */
							Label l02;

							/** The l 2. */
							Label l1;
							
							/** The l 2. */
							Label l2;

							/** The outerparent. */
							Sys outerparent;

							/**
							 * Instantiates a new inl.
							 *
							 * @param target
							 *            the target
							 */
							Inl(final Sys target) {

								this.outerparent = target;
								this.setLayout(new GridLayout(5, 2, 2, 10));

								this.l01 = new Label("Cylinder-", Label.RIGHT);
								this.l01.setForeground(Color.blue);
								this.l02 = new Label("Ball Input", Label.LEFT);
								this.l02.setForeground(Color.blue);

								this.l1 = new Label("Spin rpm", Label.CENTER);
								this.f1 = new TextField(Sys.ZERO_ZERO, 5);

								this.l2 = new Label(Sys.RADIUS_FT, Label.CENTER);
								this.f2 = new TextField(".5", 5);

								/*
								 * l3 = new Label("Span ft", Label.CENTER) ; //See previous comments f3 = new
								 * TextField("5.0",5) ;
								 */

								this.add(this.l01);
								this.add(this.l02);

								this.add(this.l1);
								this.add(this.f1);

								this.add(this.l2);
								this.add(this.f2);

								/* add(l3) ; //See previous comments add(f3) ; */

								this.add(new Label(Sys.SPACE_STR, Label.CENTER));
								this.add(new Label(Sys.SPACE_STR, Label.CENTER));
							}

							/**
							 * (non-Javadoc).
							 *
							 * @param evt
							 *            the evt
							 * @return true, if successful
							 * @see java.awt.Component#handleEvent(java.awt.Event)
							 */
							@Override
							public boolean handleEvent(final Event evt) {
								Double V1, V2;
								double v1;
								double v2;
								double v3;
								float fl1;
								int i1;
								int i2;

								if (evt.id == Event.ACTION_EVENT) {
									V1 = Double.valueOf(this.f1.getText());
									v1 = V1.doubleValue();
									V2 = Double.valueOf(this.f2.getText());
									v2 = V2.doubleValue();

									DesignCard.this.spin = v1;
									if (v1 < DesignCard.this.spinmn) {
										DesignCard.this.spin = v1 = DesignCard.this.spinmn;
										fl1 = (float) v1;
										this.f1.setText(String.valueOf(fl1));
									}
									if (v1 > DesignCard.this.spinmx) {
										DesignCard.this.spin = v1 = DesignCard.this.spinmx;
										fl1 = (float) v1;
										this.f1.setText(String.valueOf(fl1));
									}
									DesignCard.this.spin = DesignCard.this.spin / 60.0;

									DesignCard.this.radius = v2;
									if (v2 < DesignCard.this.radmn) {
										DesignCard.this.radius = v2 = DesignCard.this.radmn;
										fl1 = (float) v2;
										this.f2.setText(String.valueOf(fl1));
									}
									if (v2 > DesignCard.this.radmx) {
										DesignCard.this.radius = v2 = DesignCard.this.radmx;
										fl1 = (float) v2;
										this.f2.setText(String.valueOf(fl1));
									}
									In.this.cyl.setLims();

									v3 = DesignCard.this.span;
									if (DesignCard.this.foiltype == FOILTYPE_BALL) {
										v3 = DesignCard.this.radius;
										fl1 = (float) v3;
									}
									DesignCard.this.spanfac = (int) (DesignCard.this.fact * DesignCard.this.span
											/ DesignCard.this.radius * .3535);
									DesignCard.this.area = 2.0 * DesignCard.this.radius * DesignCard.this.span;
									if (DesignCard.this.foiltype == FOILTYPE_BALL) {
										DesignCard.this.area = Sys.PI * DesignCard.this.radius * DesignCard.this.radius;
									}

									i1 = (int) ((v1 - DesignCard.this.spinmn)
											/ (DesignCard.this.spinmx - DesignCard.this.spinmn) * 1000.);
									i2 = (int) ((v2 - DesignCard.this.radmn)
											/ (DesignCard.this.radmx - DesignCard.this.radmn) * 1000.);

									Cyl.this.inr.s1.setValue(i1);
									Cyl.this.inr.s2.setValue(i2);

									DesignCard.this.computeFlow();
									return true;
								} else {
									return false;
								}
							} // Handler
						} // Inl

						/** The Class Inr. */
						class Inr extends Panel {

							/** The Constant serialVersionUID. */
							private static final long serialVersionUID = 1L;

							/** The outerparent. */
							Sys outerparent;

							/** The s 2. */
							Scrollbar s1;

							/** The s 2. */
							Scrollbar s2 /* ,s3 */; // Span Scrollbar commented out

							/** The shapch. */
							Choice shapch;

							/**
							 * Instantiates a new inr.
							 *
							 * @param target
							 *            the target
							 */
							Inr(final Sys target) {
								int i1;
								int i2;

								this.outerparent = target;
								this.setLayout(new GridLayout(5, 1, 2, 10));

								i1 = (int) ((DesignCard.this.spin * 60.0 - DesignCard.this.spinmn)
										/ (DesignCard.this.spinmx - DesignCard.this.spinmn) * 1000.);
								i2 = (int) ((DesignCard.this.radius - DesignCard.this.radmn)
										/ (DesignCard.this.radmx - DesignCard.this.radmn) * 1000.);

								this.s1 = new Scrollbar(Scrollbar.HORIZONTAL, i1, 10, 0, 1000);
								this.s2 = new Scrollbar(Scrollbar.HORIZONTAL, i2, 10, 0, 1000);
								/* s3 = new Scrollbar(Scrollbar.HORIZONTAL,i3,10,0,1000); */ // See previous comment

								this.shapch = new Choice();
								this.shapch.addItem(Sys.AIRFOIL);
								this.shapch.addItem(Sys.ELLIPSE);
								this.shapch.addItem(Sys.PLATE);
								this.shapch.addItem("Cylinder");
								this.shapch.addItem("Ball");
								this.shapch.setBackground(Color.white);
								this.shapch.setForeground(Color.blue);
								this.shapch.select(0);

								this.add(this.shapch);
								this.add(this.s1);
								this.add(this.s2);
								/* add(s3) ; */ // See previous comment
								this.add(new Label(Sys.SPACE_STR, Label.CENTER));
							}

							/**
							 * Handle bar.
							 *
							 * @param evt
							 *            the evt
							 */
							public void handleBar(final Event evt) {
								int i1, i2;
								double v1, v2;
								float fl1, fl2;

								// Input for computations
								i1 = this.s1.getValue();
								i2 = this.s2.getValue();

								DesignCard.this.spin = v1 = i1 * (DesignCard.this.spinmx - DesignCard.this.spinmn)
										/ 1000. + DesignCard.this.spinmn;
								DesignCard.this.spin = DesignCard.this.spin / 60.0;
								DesignCard.this.radius = v2 = i2 * (DesignCard.this.radmx - DesignCard.this.radmn)
										/ 1000. + DesignCard.this.radmn;
								if (DesignCard.this.foiltype == FOILTYPE_BALL) {
									DesignCard.this.span = DesignCard.this.radius;
								}
								DesignCard.this.spanfac = (int) (DesignCard.this.fact * DesignCard.this.span
										/ DesignCard.this.radius * .3535);
								DesignCard.this.area = 2.0 * DesignCard.this.radius * DesignCard.this.span;
								if (DesignCard.this.foiltype == FOILTYPE_BALL) {
									DesignCard.this.area = Sys.PI * DesignCard.this.radius * DesignCard.this.radius;
								}
								In.this.cyl.setLims();

								fl1 = (float) v1;
								fl2 = (float) v2;

								Cyl.this.inl.f1.setText(String.valueOf(fl1));
								Cyl.this.inl.f2.setText(String.valueOf(fl2));

								DesignCard.this.computeFlow();
							}

							/**
							 * Handle cho.
							 *
							 * @param evt
							 *            the evt
							 */
							public void handleCho(final Event evt) {
								int i2;
								double v2;
								float fl1;

								DesignCard.this.foiltype = this.shapch.getSelectedIndex() + 1;
								if (DesignCard.this.foiltype >= FOILTYPE_CYLINDER) {
									DesignCard.this.alfval = 0.0;
								}
								if (DesignCard.this.foiltype <= FOILTYPE_ELLIPTICAL) {
									DesignCard.this.layin.show(Ind.this.in, MainPanel.SECOND_CARD);
								}
								if (DesignCard.this.foiltype == FOILTYPE_PLATE) {
									DesignCard.this.layin.show(Ind.this.in, MainPanel.SECOND_CARD);
									DesignCard.this.thkinpt = v2 = DesignCard.this.thkmn;
									DesignCard.this.thkval = DesignCard.this.thkinpt / 25.0;
									fl1 = (float) v2;
									Ind.this.in.shp.upr.inl.f2.setText(String.valueOf(fl1));
									i2 = (int) ((v2 - DesignCard.this.thkmn)
											/ (DesignCard.this.thkmx - DesignCard.this.thkmn) * 1000.);
									Ind.this.in.shp.upr.inr.s2.setValue(i2);
								}
								if (DesignCard.this.foiltype == FOILTYPE_CYLINDER) {
									DesignCard.this.layin.show(Ind.this.in, MainPanel.FOURTH_CARD);
								}
								if (DesignCard.this.foiltype == FOILTYPE_BALL) {
									DesignCard.this.span = DesignCard.this.radius;
									DesignCard.this.area = Sys.PI * DesignCard.this.radius * DesignCard.this.radius;
									DesignCard.this.layin.show(Ind.this.in, MainPanel.FOURTH_CARD);
									if (DesignCard.this.viewflg != WALL_VIEW_TRANSPARENT) {
										DesignCard.this.viewflg = WALL_VIEW_TRANSPARENT;
									}
								}

								Ind.this.in.shp.upr.inl.shapch.select(DesignCard.this.foiltype - 1);
								DesignCard.this.layout.show(DesignCard.this.oud, MainPanel.FIRST_CARD);
								DesignCard.this.outopt = 0;
								DesignCard.this.dispp = 0;
								// DesignCard.this.calcrange = 0;

								DesignCard.this.loadInput();
							} // handler

							/**
							 * (non-Javadoc).
							 *
							 * @param evt
							 *            the evt
							 * @return true, if successful
							 * @see java.awt.Component#handleEvent(java.awt.Event)
							 */
							@Override
							public boolean handleEvent(final Event evt) {
								if (evt.id == Event.ACTION_EVENT) {
									this.handleCho(evt);
									return true;
								}
								if (evt.id == Event.SCROLL_ABSOLUTE) {
									this.handleBar(evt);
									return true;
								}
								if (evt.id == Event.SCROLL_LINE_DOWN) {
									this.handleBar(evt);
									return true;
								}
								if (evt.id == Event.SCROLL_LINE_UP) {
									this.handleBar(evt);
									return true;
								}
								if (evt.id == Event.SCROLL_PAGE_DOWN) {
									this.handleBar(evt);
									return true;
								}
								if (evt.id == Event.SCROLL_PAGE_UP) {
									this.handleBar(evt);
									return true;
								} else {
									return false;
								}
							}
						} // Inr

						/** The Constant serialVersionUID. */
						private static final long serialVersionUID = 1L;

						/** The inl. */
						Inl inl;

						/** The inr. */
						Inr inr;

						/** The outerparent. */
						Sys outerparent;

						/**
						 * Instantiates a new cyl.
						 *
						 * @param target
						 *            the target
						 */
						Cyl(final Sys target) {

							this.outerparent = target;
							this.setLayout(new GridLayout(1, 2, 5, 5));

							this.inl = new Inl(this.outerparent);
							this.inr = new Inr(this.outerparent);

							this.add(this.inl);
							this.add(this.inr);
						}

						/** Sets the lims. */
						public void setLims() {
							float fl1;
							int i1;

							DesignCard.this.spinmx = 2.75 * DesignCard.this.vfsd / DesignCard.this.vconv
									/ (DesignCard.this.radius / DesignCard.this.lconv);
							DesignCard.this.spinmn = -2.75 * DesignCard.this.vfsd / DesignCard.this.vconv
									/ (DesignCard.this.radius / DesignCard.this.lconv);
							if (DesignCard.this.spin * 60.0 < DesignCard.this.spinmn) {
								DesignCard.this.spin = DesignCard.this.spinmn / 60.0;
								fl1 = (float) (DesignCard.this.spin * 60.0);
								this.inl.f1.setText(String.valueOf(fl1));
							}
							if (DesignCard.this.spin * 60.0 > DesignCard.this.spinmx) {
								DesignCard.this.spin = DesignCard.this.spinmx / 60.0;
								fl1 = (float) (DesignCard.this.spin * 60.0);
								this.inl.f1.setText(String.valueOf(fl1));
							}
							i1 = (int) ((60 * DesignCard.this.spin - DesignCard.this.spinmn)
									/ (DesignCard.this.spinmx - DesignCard.this.spinmn) * 1000.);
							this.inr.s1.setValue(i1);
						}
					} // Cyl

					/** The Class Shp. */
					class Shp extends Panel {

						/** The Class Lwr. */
						class Lwr extends Panel {

							/** The Class Dwn. */
							class Dwn extends Panel {

								/** The Constant serialVersionUID. */
								private static final long serialVersionUID = 1L;

								/** The endit. */
								Button resetBtn;

								/** The endit. */
								Button returnBtn;

								/** The l 4. */
								Label l3;

								/** The l 4. */
								Label l4;

								/** The o 6. */
								TextField o1;

								/** The o 2. */
								TextField o2;

								/** The o 3. */
								TextField o3;

								/** The o 4. */
								TextField o4;

								/** The o 5. */
								TextField o5;

								/** The o 6. */
								TextField o6;

								/** The outerparent. */
								Sys outerparent;

								/** The untch. */
								Choice untch;

								/**
								 * Instantiates a new dwn.
								 *
								 * @param target
								 *            the target
								 */
								Dwn(final Sys target) {
									this.outerparent = target;
									this.setLayout(new GridLayout(3, 4, 5, 5));

									this.l3 = new Label("Area-sq ft", Label.CENTER);
									this.o3 = new TextField("100.0", 5);
									this.o3.setBackground(Color.black);
									this.o3.setForeground(Color.yellow);

									this.l4 = new Label("Aspect Ratio", Label.CENTER);
									this.o4 = new TextField(Sys.ZERO_ZERO, 5);
									this.o4.setBackground(Color.black);
									this.o4.setForeground(Color.yellow);

									this.o1 = new TextField(Sys.ZERO_ZERO, 5);
									this.o1.setBackground(Color.black);
									this.o1.setForeground(Color.yellow);

									this.o2 = new TextField(Sys.ZERO_ZERO, 5);
									this.o2.setBackground(Color.black);
									this.o2.setForeground(Color.yellow);

									this.o5 = new TextField(Sys.ZERO_ZERO, 5);
									this.o5.setBackground(Color.black);
									this.o5.setForeground(Color.yellow);

									this.o6 = new TextField(Sys.ZERO_ZERO, 5);
									this.o6.setBackground(Color.black);
									this.o6.setForeground(Color.yellow);

									this.resetBtn = new Button(Sys.RESET);
									this.resetBtn.setBackground(Color.magenta);
									this.resetBtn.setForeground(Color.white);

									this.returnBtn = new Button(Sys.RETURN);
									this.returnBtn.setBackground(Color.red);
									this.returnBtn.setForeground(Color.white);

									this.untch = new Choice();
									this.untch.addItem("Imperial");
									this.untch.addItem("Metric");
									this.untch.setBackground(Color.white);
									this.untch.setForeground(Color.red);
									this.untch.select(0);

									this.add(new Label(Sys.SPACE_STR, Label.CENTER));
									this.add(new Label(Sys.SPACE_STR, Label.CENTER));
									this.add(new Label(Sys.SPACE_STR, Label.CENTER));
									this.add(new Label(Sys.SPACE_STR, Label.CENTER));
									/* add(o1) ; add(o2) ; add(o5) ; add(o6) ; */
									this.add(this.l3);
									this.add(this.o3);
									this.add(this.l4);
									this.add(this.o4);

									this.add(this.returnBtn);
									this.add(this.resetBtn);
									this.add(new Label(" Units:", Label.RIGHT));
									this.add(this.untch);
								}

								/**
								 * (non-Javadoc).
								 *
								 * @param evt
								 *            the evt
								 * @param arg
								 *            the arg
								 * @return true, if successful
								 * @see java.awt.Component#action(java.awt.Event, java.lang.Object)
								 */
								@Override
								public boolean action(final Event evt, final Object arg) {
									if (evt.target instanceof Button) {
										this.handleRefs(evt, arg);
										return true;
									}
									if (evt.target instanceof Choice) {
										DesignCard.this.lunits = this.untch.getSelectedIndex();
										// **** the lunits check MUST come first
										DesignCard.this.setUnits();

										DesignCard.this.loadInput();

										return true;
									} else {
										return false;
									}
								} // end action

								/**
								 * Handle refs.
								 *
								 * @param evt
								 *            the evt
								 * @param arg
								 *            the arg
								 */
								public void handleRefs(final Event evt, final Object arg) {
									final String label = (String) arg;
									int datos, datps;

									if (label.equals(Sys.RESET)) {
										datos = DesignCard.this.dato;
										datps = DesignCard.this.datp;
										DesignCard.this.solved.setDefaults();
										DesignCard.this.ind.in.shp.upr.inl.shapch.select(0);
										DesignCard.this.ind.in.cyl.inr.shapch.select(0);
										DesignCard.this.ind.in.shp.lwr.dwn.untch.select(DesignCard.this.lunits);
										// **** the lunits check MUST come first
										DesignCard.this.setUnits();
										DesignCard.this.layout.show(DesignCard.this.oud, MainPanel.FIRST_CARD);
										DesignCard.this.outopt = 0;
										DesignCard.this.dato = datos;
										DesignCard.this.datp = datps;

										DesignCard.this.loadInput();
									}

									if (label.equals(Sys.RETURN)) {
										Sys.this.mainPannel.mainMenu.f1.setText("Completed");
										Sys.this.mainPannel.mainMenu.f1.setForeground(Color.red);
										Sys.this.mainPannel.mainMenu.showFirstCard();
									}

								} // end handler
							} // end Lwr

							/** The Class Upa. */
							class Upa extends Panel {

								/** The Class Lft. */
								class Lft extends Panel {

									/** The Constant serialVersionUID. */
									private static final long serialVersionUID = 1L;

									/**
									 * Instantiates a new lft.
									 *
									 * @param target
									 *            the target
									 */
									Lft(final Sys target) {
										Upa.this.outerparent = target;
										this.setLayout(new GridLayout(3, 1, 2, 2));

										this.add(new Label(Sys.SPACE_STR, Label.CENTER));
										this.add(new Label(Sys.SPACE_STR, Label.CENTER));
										this.add(new Label(Sys.SPACE_STR, Label.CENTER));
									}

									/**
									 * (non-Javadoc).
									 *
									 * @param evt
									 *            the evt
									 * @return true, if successful
									 * @see java.awt.Component#handleEvent(java.awt.Event)
									 */
									@Override
									public boolean handleEvent(final Event evt) {

										if (evt.id == Event.ACTION_EVENT) {
											DesignCard.this.dato = 1;
											DesignCard.this.datp = 1;
											return true;
										} else {
											return false;
										}
									}
								} // end Lft

								/** The Class Rit. */
								class Rit extends Panel {

									/** The Constant serialVersionUID. */
									private static final long serialVersionUID = 1L;

									/** The save data btn. */
									Button saveDataBtn;

									/** The outerparent. */
									Sys outerparent;

									/**
									 * Instantiates a new rit.
									 *
									 * @param target
									 *            the target
									 */
									Rit(final Sys target) {
										this.outerparent = target;
										this.setLayout(new GridLayout(3, 2, 2, 2));

										this.saveDataBtn = new Button("Save Data");
										this.saveDataBtn.setBackground(Color.blue);
										this.saveDataBtn.setForeground(Color.white);

										this.add(new Label(Sys.SPACE_STR, Label.CENTER));
										this.add(new Label(Sys.SPACE_STR, Label.CENTER));

										this.add(new Label(Sys.SPACE_STR, Label.CENTER));
										this.add(new Label(Sys.SPACE_STR, Label.CENTER));

										this.add(this.saveDataBtn);
										this.add(new Label(Sys.SPACE_STR, Label.CENTER));
									}

									/**
									 * (non-Javadoc).
									 *
									 * @param evt
									 *            the evt
									 * @param arg
									 *            the arg
									 * @return true, if successful
									 * @see java.awt.Component#action(java.awt.Event, java.lang.Object)
									 */
									@Override
									public boolean action(final Event evt, final Object arg) {
										if (evt.target instanceof Button) {
											this.handleRefs(evt, arg);
											return true;
										} else {
											return false;
										}
									} // end handler

									/**
									 * Handle refs.
									 *
									 * @param evt
									 *            the evt
									 * @param arg
									 *            the arg
									 */
									public void handleRefs(final Event evt, final Object arg) {
										final String label = (String) arg;
										if (label.equals("Save Data")) {
											Sys.acam[Sys.this.counter] = DesignCard.this.camval;
											Sys.athk[Sys.this.counter] = DesignCard.this.thkval;
											Sys.aspan[Sys.this.counter] = DesignCard.this.span;
											Sys.achrd[Sys.this.counter] = DesignCard.this.chord;
											Sys.acount[Sys.this.counter] = Sys.this.counter;
											Sys.aftp[Sys.this.counter] = DesignCard.this.foiltype;
											Sys.alunits[Sys.this.counter] = DesignCard.this.lunits;
											Sys.this.counter = Sys.this.counter + 1;
										}
									} // end handleRef
								} // end Rit

								/** The Constant serialVersionUID. */
								private static final long serialVersionUID = 1L;

								/** The lft. */
								Lft lft;

								/** The outerparent. */
								Sys outerparent;

								/** The rit. */
								Rit rit;

								/**
								 * Instantiates a new upa.
								 *
								 * @param target
								 *            the target
								 */
								Upa(final Sys target) {
									this.outerparent = target;
									this.setLayout(new GridLayout(1, 2, 5, 5));

									this.lft = new Lft(this.outerparent);
									this.rit = new Rit(this.outerparent);

									this.add(this.lft);
									this.add(this.rit);
								}
							} // end Upa

							/** The Constant serialVersionUID. */
							private static final long serialVersionUID = 1L;

							/** The dwn. */
							Dwn dwn;

							/** The outerparent. */
							Sys outerparent;

							/** The upa. */
							Upa upa;

							/**
							 * Instantiates a new lwr.
							 *
							 * @param target
							 *            the target
							 */
							Lwr(final Sys target) {

								this.outerparent = target;
								this.setLayout(new GridLayout(2, 1, 5, 5));

								this.upa = new Upa(this.outerparent);
								this.dwn = new Dwn(this.outerparent);

								this.add(this.upa);
								this.add(this.dwn);
							}
						} // end Dwn

						/** The Class Upr. */
						class Upr extends Panel {

							/** The Class Inl. */
							class Inl extends Panel {

								/** The Constant serialVersionUID. */
								private static final long serialVersionUID = 1L;

								/** The f 3. */
								TextField f1, f2, f3 /* ,f4 */; // TextField commented out from Span Scrollbar space

								/** The l 02. */
								Label l01, l02;

								/** The l 3. */
								Label l1, l2, l3 /* ,l4 */; // Label commented out from Span Scrollbar.

								/** The outerparent. */
								Sys outerparent;

								/** The shapch. */
								Choice shapch;

								/**
								 * Instantiates a new inl.
								 *
								 * @param target
								 *            the target
								 */
								Inl(final Sys target) {

									this.outerparent = target;
									this.setLayout(new GridLayout(5, 2, 2, 10));

									this.l02 = new Label("Label:", Label.RIGHT);
									this.l02.setForeground(Color.blue);

									this.l1 = new Label("Camber-%c", Label.CENTER);
									this.f1 = new TextField(Sys.ZERO_ZERO, 5);

									this.l2 = new Label("Thick-%crd", Label.CENTER);
									this.f2 = new TextField(Sys.TWELVE_FIVE, 5);

									this.l3 = new Label("Chord-ft", Label.CENTER);
									this.f3 = new TextField("1.0", 5);

									/*
									 * l4 = new Label("Span-ft", Label.CENTER) ; //See previous comments f4 = new
									 * TextField("20.0",5) ;
									 */

									this.shapch = new Choice();
									this.shapch.addItem(Sys.AIRFOIL);
									this.shapch.addItem(Sys.ELLIPSE);
									this.shapch.addItem(Sys.PLATE);
									this.shapch.addItem("Cylinder");
									// shapch.addItem("Ball");
									this.shapch.setBackground(Color.white);
									this.shapch.setForeground(Color.blue);
									this.shapch.select(0);

									this.add(this.shapch);
									this.add(new Label(Sys.SPACE_STR, Label.CENTER));

									this.add(this.l1);
									this.add(this.f1);

									this.add(this.l2);
									this.add(this.f2);

									this.add(this.l3);
									this.add(this.f3);

									this.add(new Label(Sys.SPACE_STR, Label.CENTER));
									this.add(this.l02);
									/* add(l4) ; //See previous comments add(f4) ; */
								}

								/**
								 * (non-Javadoc).
								 *
								 * @param evt
								 *            the evt
								 * @return true, if successful
								 * @see java.awt.Component#handleEvent(java.awt.Event)
								 */
								@Override
								public boolean handleEvent(final Event evt) {
									Double V1;
									Double V2;
									Double V3;
									double v1;
									double v2;
									double v3;
									float fl1;
									int i1, i2, i3, choice;

									if (evt.id == Event.ACTION_EVENT) {
										DesignCard.this.foiltype = this.shapch.getSelectedIndex() + 1;
										if (DesignCard.this.foiltype == FOILTYPE_PLATE) {
											DesignCard.this.thkinpt = v2 = DesignCard.this.thkmn;
											DesignCard.this.thkval = DesignCard.this.thkinpt / 25.0;
											fl1 = (float) v2;
											this.f2.setText(String.valueOf(fl1));
											i2 = (int) ((v2 - DesignCard.this.thkmn)
													/ (DesignCard.this.thkmx - DesignCard.this.thkmn) * 1000.);
											Upr.this.inr.s2.setValue(i2);
										}

										Ind.this.in.cyl.inr.shapch.select(DesignCard.this.foiltype - 1);
										DesignCard.this.layout.show(DesignCard.this.oud, MainPanel.FIRST_CARD);
										DesignCard.this.outopt = 0;
										DesignCard.this.dispp = 0;
										// DesignCard.this.calcrange = 0;

										V1 = Double.valueOf(this.f1.getText());
										v1 = V1.doubleValue();
										V2 = Double.valueOf(this.f2.getText());
										v2 = V2.doubleValue();
										V3 = Double.valueOf(this.f3.getText());
										v3 = V3.doubleValue();

										DesignCard.this.caminpt = v1;
										if (v1 < DesignCard.this.camn) {
											DesignCard.this.caminpt = v1 = DesignCard.this.camn;
											fl1 = (float) v1;
											this.f1.setText(String.valueOf(fl1));
										}
										if (v1 > DesignCard.this.camx) {
											DesignCard.this.caminpt = v1 = DesignCard.this.camx;
											fl1 = (float) v1;
											this.f1.setText(String.valueOf(fl1));
										}
										DesignCard.this.camval = DesignCard.this.caminpt / 25.0;

										DesignCard.this.thkinpt = v2;
										if (v2 < DesignCard.this.thkmn) {
											DesignCard.this.thkinpt = v2 = DesignCard.this.thkmn;
											fl1 = (float) v2;
											this.f2.setText(String.valueOf(fl1));
										}
										if (v2 > DesignCard.this.thkmx) {
											DesignCard.this.thkinpt = v2 = DesignCard.this.thkmx;
											fl1 = (float) v2;
											this.f2.setText(String.valueOf(fl1));
										}
										DesignCard.this.thkval = DesignCard.this.thkinpt / 25.0;

										DesignCard.this.chord = v3;
										if (v3 < DesignCard.this.chrdmn) {
											DesignCard.this.chord = v3 = DesignCard.this.chrdmn;
											fl1 = (float) v3;
											this.f3.setText(String.valueOf(fl1));
										}
										if (v3 > DesignCard.this.chrdmx) {
											DesignCard.this.chord = v3 = DesignCard.this.chrdmx;
											fl1 = (float) v3;
											this.f3.setText(String.valueOf(fl1));
										}

										// keeping consistent
										choice = 3;
										if (DesignCard.this.chord >= DesignCard.this.chrdold + .01
												|| DesignCard.this.chord <= DesignCard.this.chrdold - .01) {
											choice = 1;
										}
										if (DesignCard.this.span >= DesignCard.this.spnold + .1
												|| DesignCard.this.span <= DesignCard.this.spnold - .1) {
											choice = 2;
										}
										switch (choice) {
										case 1: { // chord changed
											chordChanged();
											break;
										}
										case 2: { // span changed
											v3 = spanChanged(v3);
											break;
										}
										case 3: { // nothing changed
											nothingChanged();
											break;
										}
										}
										DesignCard.this.spanfac = (int) (2.0 * DesignCard.this.fact
												* DesignCard.this.aspr * .3535);

										Shp.this.lwr.dwn.o3.setText(String.valueOf(Sys.filter3(DesignCard.this.area)));
										Shp.this.lwr.dwn.o4.setText(String.valueOf(Sys.filter3(DesignCard.this.aspr)));

										i1 = (int) ((v1 - DesignCard.this.camn)
												/ (DesignCard.this.camx - DesignCard.this.camn) * 1000.);
										i2 = (int) ((v2 - DesignCard.this.thkmn)
												/ (DesignCard.this.thkmx - DesignCard.this.thkmn) * 1000.);
										i3 = (int) ((v3 - DesignCard.this.chrdmn)
												/ (DesignCard.this.chrdmx - DesignCard.this.chrdmn) * 1000.);

										Upr.this.inr.s1.setValue(i1);
										Upr.this.inr.s2.setValue(i2);
										Upr.this.inr.s3.setValue(i3);

										DesignCard.this.computeFlow();
										return true;
									} else {
										return false;
									}
								} // Handler

								/**
								 * Nothing changed.
								 */
								private void nothingChanged() {
									DesignCard.this.chrdold = DesignCard.this.chord;
									DesignCard.this.spnold = DesignCard.this.span;
								}

								/**
								 * Span changed.
								 *
								 * @param v3
								 *            the v 3
								 * @return the double
								 */
								private double spanChanged(double v3) {
									float fl1;
									if (DesignCard.this.span > DesignCard.this.chord) {
										DesignCard.this.area = DesignCard.this.span * DesignCard.this.chord;
										DesignCard.this.aspr = DesignCard.this.span * DesignCard.this.span
												/ DesignCard.this.area;
									}
									if (DesignCard.this.span <= DesignCard.this.chord) {
										v3 = DesignCard.this.span;
										DesignCard.this.aspr = 1.0;
										DesignCard.this.area = v3 * DesignCard.this.span;
										fl1 = (float) v3;
										this.f3.setText(String.valueOf(fl1));
										DesignCard.this.chord = v3;
										DesignCard.this.fact = DesignCard.this.fact * DesignCard.this.chord
												/ DesignCard.this.chrdold;
										DesignCard.this.factp = DesignCard.this.factp * DesignCard.this.chord
												/ DesignCard.this.chrdold;
										DesignCard.this.chrdold = DesignCard.this.chord;
									}
									DesignCard.this.spnold = DesignCard.this.span;
									return v3;
								}

								/**
								 * Chord changed.
								 */
								private void chordChanged() {
									double v4;
									if (DesignCard.this.chord < DesignCard.this.span) {
										DesignCard.this.area = DesignCard.this.span * DesignCard.this.chord;
										DesignCard.this.aspr = DesignCard.this.span * DesignCard.this.span
												/ DesignCard.this.area;
									}
									if (DesignCard.this.chord >= DesignCard.this.span) {
										v4 = DesignCard.this.chord;
										DesignCard.this.aspr = 1.0;
										DesignCard.this.area = v4 * DesignCard.this.chord;
										DesignCard.this.spnold = v4;
									}
									DesignCard.this.fact = DesignCard.this.fact * DesignCard.this.chord
											/ DesignCard.this.chrdold;
									DesignCard.this.factp = DesignCard.this.factp * DesignCard.this.chord
											/ DesignCard.this.chrdold;
									DesignCard.this.chrdold = DesignCard.this.chord;
								}
							} // Inl

							/** The Class Inr. */
							class Inr extends Panel {

								/** The Constant serialVersionUID. */
								private static final long serialVersionUID = 1L;

								/** The labl. */
								TextField labl;

								/** The outerparent. */
								Sys outerparent;

								/** The s 3. */
								Scrollbar s1, s2, s3/* ,s4 */; // Former scrollbar for Span values commented out

								/**
								 * Instantiates a new inr.
								 *
								 * @param target
								 *            the target
								 */
								Inr(final Sys target) {
									int i1, i2, i3;

									this.outerparent = target;
									this.setLayout(new GridLayout(5, 1, 2, 10));

									i1 = (int) ((0.0 - DesignCard.this.camn)
											/ (DesignCard.this.camx - DesignCard.this.camn) * 1000.);
									i2 = (int) ((12.5 - DesignCard.this.thkmn)
											/ (DesignCard.this.thkmx - DesignCard.this.thkmn) * 1000.);
									i3 = (int) ((DesignCard.this.chord - DesignCard.this.chrdmn)
											/ (DesignCard.this.chrdmx - DesignCard.this.chrdmn) * 1000.);

									this.s1 = new Scrollbar(Scrollbar.HORIZONTAL, i1, 10, 0, 1000);
									this.s2 = new Scrollbar(Scrollbar.HORIZONTAL, i2, 10, 0, 1000);
									this.s3 = new Scrollbar(Scrollbar.HORIZONTAL, i3, 10, 0, 1000);
									/* s4 = new Scrollbar(Scrollbar.HORIZONTAL,i4,10,0,1000); */
									// See previous comment

									this.labl = new TextField(" Enter Name ");

									this.add(new Label(Sys.SPACE_STR, Label.CENTER));
									this.add(this.s1);
									this.add(this.s2);
									this.add(this.s3);
									this.add(this.labl);
									/* add(s4) ; */
								}

								/**
								 * Handle bar.
								 *
								 * @param evt
								 *            the evt
								 */
								public void handleBar(final Event evt) {
									int i1, i2, i3, choice;
									double v1, v2, v3, v4;
									float fl1, fl2, fl3;

									// Input for computations
									i1 = this.s1.getValue();
									i2 = this.s2.getValue();
									i3 = this.s3.getValue();

									DesignCard.this.caminpt = v1 = i1 * (DesignCard.this.camx - DesignCard.this.camn)
											/ 1000. + DesignCard.this.camn;
									DesignCard.this.camval = DesignCard.this.caminpt / 25.0;
									DesignCard.this.thkinpt = v2 = i2 * (DesignCard.this.thkmx - DesignCard.this.thkmn)
											/ 1000. + DesignCard.this.thkmn;
									DesignCard.this.thkval = DesignCard.this.thkinpt / 25.0;
									DesignCard.this.chord = v3 = i3 * (DesignCard.this.chrdmx - DesignCard.this.chrdmn)
											/ 1000. + DesignCard.this.chrdmn;
									v4 = DesignCard.this.span;

									// keeping consistent
									choice = 0;
									if (DesignCard.this.chord >= DesignCard.this.chrdold + .01
											|| DesignCard.this.chord <= DesignCard.this.chrdold - .01) {
										choice = 1;
									}
									if (DesignCard.this.span >= DesignCard.this.spnold + .1
											|| DesignCard.this.span <= DesignCard.this.spnold - .1) {
										choice = 2;
									}
									switch (choice) {
									case 1: { // chord changed
										chordChanged(v4);
										break;
									}
									case 2: { // span changed
										v3 = spanChanged(v3);
										break;
									}
									}
									DesignCard.this.spanfac = (int) (2.0 * DesignCard.this.fact * DesignCard.this.aspr
											* .3535);
									Shp.this.lwr.dwn.o3.setText(String.valueOf(Sys.filter3(DesignCard.this.area)));
									Shp.this.lwr.dwn.o4.setText(String.valueOf(Sys.filter3(DesignCard.this.aspr)));

									fl1 = (float) v1;
									fl2 = (float) v2;
									fl3 = Sys.filter3(v3);

									Upr.this.inl.f1.setText(String.valueOf(fl1));
									Upr.this.inl.f2.setText(String.valueOf(fl2));
									Upr.this.inl.f3.setText(String.valueOf(fl3));

									DesignCard.this.computeFlow();
								}

								/**
								 * Span changed.
								 *
								 * @param v3
								 *            the v 3
								 * @return the double
								 */
								private double spanChanged(double v3) {
									int i3;
									if (DesignCard.this.span > DesignCard.this.chord) {
										DesignCard.this.area = DesignCard.this.span * DesignCard.this.chord;
										DesignCard.this.aspr = DesignCard.this.span * DesignCard.this.span
												/ DesignCard.this.area;
									}
									if (DesignCard.this.span <= DesignCard.this.chord) {
										v3 = DesignCard.this.span;
										DesignCard.this.aspr = 1.0;
										DesignCard.this.area = v3 * DesignCard.this.span;
										DesignCard.this.chord = v3;
										DesignCard.this.fact = DesignCard.this.fact * DesignCard.this.chord
												/ DesignCard.this.chrdold;
										DesignCard.this.factp = DesignCard.this.factp * DesignCard.this.chord
												/ DesignCard.this.chrdold;
										DesignCard.this.chrdold = DesignCard.this.chord;
										i3 = (int) ((DesignCard.this.chord - DesignCard.this.chrdmn)
												/ (DesignCard.this.chrdmx - DesignCard.this.chrdmn) * 1000.);
										this.s3.setValue(i3);
									}
									DesignCard.this.spnold = DesignCard.this.span;
									return v3;
								}

								/**
								 * Chord changed.
								 *
								 * @param v4
								 *            the v 4
								 */
								private void chordChanged(double v4) {
									if (DesignCard.this.chord < DesignCard.this.span) {
										DesignCard.this.area = DesignCard.this.span * DesignCard.this.chord;
										DesignCard.this.aspr = DesignCard.this.span * DesignCard.this.span
												/ DesignCard.this.area;
									}
									if (DesignCard.this.chord >= DesignCard.this.span) {
										DesignCard.this.chord = v4;
										DesignCard.this.aspr = 1.0;
										DesignCard.this.area = v4 * DesignCard.this.chord;
										DesignCard.this.spnold = v4;
									}
									DesignCard.this.fact = DesignCard.this.fact * DesignCard.this.chord
											/ DesignCard.this.chrdold;
									DesignCard.this.factp = DesignCard.this.factp * DesignCard.this.chord
											/ DesignCard.this.chrdold;
									DesignCard.this.chrdold = DesignCard.this.chord;
								}

								/**
								 * (non-Javadoc).
								 *
								 * @param evt
								 *            the evt
								 * @return true, if successful
								 * @see java.awt.Component#handleEvent(java.awt.Event)
								 */
								@Override
								public boolean handleEvent(final Event evt) {
									if (evt.id == Event.ACTION_EVENT) {
										this.handleTxt(evt);
										return true;
									}
									if (evt.id == Event.SCROLL_ABSOLUTE) {
										this.handleBar(evt);
										return true;
									}
									if (evt.id == Event.SCROLL_LINE_DOWN) {
										this.handleBar(evt);
										return true;
									}
									if (evt.id == Event.SCROLL_LINE_UP) {
										this.handleBar(evt);
										return true;
									}
									if (evt.id == Event.SCROLL_PAGE_DOWN) {
										this.handleBar(evt);
										return true;
									}
									if (evt.id == Event.SCROLL_PAGE_UP) {
										this.handleBar(evt);
										return true;
									} else {
										return false;
									}
								}

								/**
								 * Handle txt.
								 *
								 * @param evt
								 *            the evt
								 */
								public void handleTxt(final Event evt) {
								}

							} // Inr

							/** The Constant serialVersionUID. */
							private static final long serialVersionUID = 1L;

							/** The inl. */
							Inl inl;

							/** The inr. */
							Inr inr;

							/** The outerparent. */
							Sys outerparent;

							/**
							 * Instantiates a new upr.
							 *
							 * @param target
							 *            the target
							 */
							Upr(final Sys target) {

								this.outerparent = target;
								this.setLayout(new GridLayout(1, 2, 5, 5));

								this.inl = new Inl(this.outerparent);
								this.inr = new Inr(this.outerparent);

								this.add(this.inl);
								this.add(this.inr);
							}
						} // end Upr

						/** The Constant serialVersionUID. */
						private static final long serialVersionUID = 1L;

						/** The lwr. */
						Lwr lwr;

						/** The outerparent. */
						Sys outerparent;

						/** The upr. */
						Upr upr;

						/**
						 * Instantiates a new shp.
						 *
						 * @param target
						 *            the target
						 */
						Shp(final Sys target) {

							this.outerparent = target;
							this.setLayout(new GridLayout(2, 1, 5, 5));

							this.upr = new Upr(this.outerparent);
							this.lwr = new Lwr(this.outerparent);

							this.add(this.upr);
							this.add(this.lwr);
						}
					} // Shp

					/** The Constant serialVersionUID. */
					private static final long serialVersionUID = 1L;

					/** The cyl. */
					Cyl cyl;

					/** The outerparent. */
					Sys outerparent;

					/** The shp. */
					Shp shp;

					/**
					 * Instantiates a new in.
					 *
					 * @param target
					 *            the target
					 */
					In(final Sys target) {
						this.outerparent = target;
						DesignCard.this.layin = new CardLayout();
						this.setLayout(DesignCard.this.layin);

						this.shp = new Shp(this.outerparent);
						this.cyl = new Cyl(this.outerparent);

						this.add(MainPanel.SECOND_CARD, this.shp);
						this.add(MainPanel.FOURTH_CARD, this.cyl);
					}
				} // In

				/** The Constant serialVersionUID. */
				private static final long serialVersionUID = 1L;

				/** The in. */
				In in;

				/** The outerparent. */
				Sys outerparent;

				/** The view. */
				DesignViewer view;

				/**
				 * Instantiates a new ind.
				 *
				 * @param target
				 *            the target
				 */
				Ind(final Sys target) {
					this.outerparent = target;
					this.setLayout(new GridLayout(2, 1, 5, 5));

					this.view = new DesignViewer(this.outerparent);
					this.in = new In(this.outerparent);

					this.add(this.view);
					this.add(this.in);
				}
			} // Ind

			/** The Class Oud. */
			class Oud extends Panel {

				/** The Class Plt. */
				class OudPlotter extends Canvas implements Runnable {

					/** The Constant serialVersionUID. */
					private static final long serialVersionUID = 1L;

					/** The ancp. */
					Point locp, ancp;

					/** The outerparent. */
					Sys outerparent;

					/** The run 2. */
					Thread run2;

					/**
					 * Instantiates a new plt.
					 *
					 * @param target
					 *            the target
					 */
					OudPlotter(final Sys target) {
						this.setBackground(Color.blue);
						this.run2 = null;
					}

					/**
					 * Handleb.
					 *
					 * @param x
					 *            the x
					 * @param y
					 *            the y
					 */
					public void handleb(final int x, final int y) {
						if (x >= 235 && x <= 305) {
							if (y >= 0 && y <= 20) { // increase by 2
								DesignCard.this.plscale = DesignCard.this.plscale * 2.0;
							}
							if (y >= 50 && y <= 70) { // decrease by 2
								DesignCard.this.plscale = DesignCard.this.plscale / 2.0;
							}
						}
						DesignCard.this.oud.plotter.repaint();
					}

					/**
					 * (non-Javadoc).
					 *
					 * @param evt
					 *            the evt
					 * @param x
					 *            the x
					 * @param y
					 *            the y
					 * @return true, if successful
					 * @see java.awt.Component#mouseUp(java.awt.Event, int, int)
					 */
					@Override
					public boolean mouseUp(final Event evt, final int x, final int y) {
						this.handleb(x, y);
						return true;
					}

					/**
					 * (non-Javadoc).
					 *
					 * @param g
					 *            the g
					 * @see java.awt.Canvas#paint(java.awt.Graphics)
					 */
					@Override
					public void paint(final Graphics g) {
						if (Sys.this.mainPannel.designCard.isVisible()) {
							int i, j;
							int n;
							int inmax, inmin;
							final int exes[] = new int[8];
							final int whys[] = new int[8];
							double xl, yl;
							double ydisp;
							final int camx[] = new int[19];
							final int camy[] = new int[19];
							new Color(0, 0, 0);
							Sys.this.oudPlotterImgBufGraphContext.setColor(Color.white);
							Sys.this.oudPlotterImgBufGraphContext.fillRect(0, 0, Sys.OUD_PLOTTER_WIDTH,
									Sys.OUD_PLOTTER_HEIGHT);

							// graph paper
							xl = 0.0 + DesignCard.this.xtp;
							yl = 0.0 + DesignCard.this.ytp;

							Sys.this.oudPlotterImgBufGraphContext.setColor(Color.blue);
							for (j = 0; j <= 40; ++j) {
								exes[0] = 0;
								exes[1] = 500;
								whys[0] = whys[1] = (int) (yl + 100. / DesignCard.this.graphconv * j);
								Sys.this.oudPlotterImgBufGraphContext.drawLine(exes[0], whys[0], exes[1], whys[1]);
								whys[0] = whys[1] = (int) (yl - 100. / DesignCard.this.graphconv * j);
								Sys.this.oudPlotterImgBufGraphContext.drawLine(exes[0], whys[0], exes[1], whys[1]);
							}
							for (j = 0; j <= 20; ++j) {
								whys[0] = 0;
								whys[1] = 1000;
								exes[0] = exes[1] = (int) (xl + 100. / DesignCard.this.graphconv * j);
								Sys.this.oudPlotterImgBufGraphContext.drawLine(exes[0], whys[0], exes[1], whys[1]);
								exes[0] = exes[1] = (int) (xl - 100. / DesignCard.this.graphconv * j);
								Sys.this.oudPlotterImgBufGraphContext.drawLine(exes[0], whys[0], exes[1], whys[1]);
							}

							// top view
							ydisp = 100.0;
							inmax = 1;
							inmin = 1;
							for (n = 1; n <= DesignCard.this.nptc; ++n) {
								if (DesignCard.this.xpl[0][n] > DesignCard.this.xpl[0][inmax]) {
									inmax = n;
								}
								if (DesignCard.this.xpl[0][n] < DesignCard.this.xpl[0][inmin]) {
									inmin = n;
								}
							}
							Sys.this.oudPlotterImgBufGraphContext.setColor(Color.black);
							exes[0] = (int) (DesignCard.this.plscale * DesignCard.this.factp
									* DesignCard.this.xpl[0][inmin]) + DesignCard.this.xtp;
							whys[0] = (int) (DesignCard.this.plscale * 100. * 0.0 - ydisp) + DesignCard.this.ytp;
							exes[1] = (int) (DesignCard.this.plscale * DesignCard.this.factp
									* DesignCard.this.xpl[0][inmax]) + DesignCard.this.xtp;
							whys[1] = (int) (DesignCard.this.plscale * 100. * 0.0 - ydisp) + DesignCard.this.ytp;
							exes[2] = (int) (DesignCard.this.plscale * DesignCard.this.factp
									* DesignCard.this.xpl[0][inmax]) + DesignCard.this.xtp;
							whys[2] = (int) (DesignCard.this.plscale * 100.
									* (-DesignCard.this.span / DesignCard.this.lconv) - ydisp) + DesignCard.this.ytp;
							exes[3] = (int) (DesignCard.this.plscale * DesignCard.this.factp
									* DesignCard.this.xpl[0][inmin]) + DesignCard.this.xtp;
							whys[3] = (int) (DesignCard.this.plscale * 100.
									* (-DesignCard.this.span / DesignCard.this.lconv) - ydisp) + DesignCard.this.ytp;
							Sys.this.oudPlotterImgBufGraphContext.fillPolygon(exes, whys, 4);
							Sys.this.oudPlotterImgBufGraphContext.setColor(Color.green);
							Sys.this.oudPlotterImgBufGraphContext.drawString("Span", exes[0] + 10,
									(whys[0] + whys[2]) / 2);
							if (DesignCard.this.foiltype <= FOILTYPE_PLATE) {
								Sys.this.oudPlotterImgBufGraphContext.drawString(Sys.CHORD2,
										(exes[0] + exes[1]) / 2 - 20, whys[0] - 10);
							}
							if (DesignCard.this.foiltype == FOILTYPE_CYLINDER) {
								Sys.this.oudPlotterImgBufGraphContext.drawString(Sys.DIAMETER, exes[2] + 10, 55);
							}
							// draw the airfoil geometry
							Sys.this.oudPlotterImgBufGraphContext.setColor(Color.black);
							exes[1] = (int) (DesignCard.this.plscale * DesignCard.this.factp
									* DesignCard.this.xpl[0][DesignCard.this.npt2]) + DesignCard.this.xtp;

							whys[1] = (int) (DesignCard.this.plscale * DesignCard.this.factp
									* -DesignCard.this.ypl[0][DesignCard.this.npt2]) + DesignCard.this.ytp;

							exes[2] = (int) (DesignCard.this.plscale * DesignCard.this.factp
									* DesignCard.this.xpl[0][DesignCard.this.npt2]) + DesignCard.this.xtp;

							whys[2] = (int) (DesignCard.this.plscale * DesignCard.this.factp
									* -DesignCard.this.ypl[0][DesignCard.this.npt2]) + DesignCard.this.ytp;

							for (i = 1; i <= DesignCard.this.npt2 - 1; ++i) {
								exes[0] = exes[1];
								whys[0] = whys[1];
								exes[1] = (int) (DesignCard.this.plscale * DesignCard.this.factp
										* DesignCard.this.xpl[0][DesignCard.this.npt2 - i]) + DesignCard.this.xtp;

								whys[1] = (int) (DesignCard.this.plscale * DesignCard.this.factp
										* -DesignCard.this.ypl[0][DesignCard.this.npt2 - i]) + DesignCard.this.ytp;

								exes[3] = exes[2];
								whys[3] = whys[2];
								exes[2] = (int) (DesignCard.this.plscale * DesignCard.this.factp
										* DesignCard.this.xpl[0][DesignCard.this.npt2 + i]) + DesignCard.this.xtp;

								whys[2] = (int) (DesignCard.this.plscale * DesignCard.this.factp
										* -DesignCard.this.ypl[0][DesignCard.this.npt2 + i]) + DesignCard.this.ytp;

								camx[i] = (exes[1] + exes[2]) / 2;
								camy[i] = (whys[1] + whys[2]) / 2;
								if (DesignCard.this.foiltype == FOILTYPE_PLATE) {
									Sys.this.oudPlotterImgBufGraphContext.setColor(Color.yellow);
									Sys.this.oudPlotterImgBufGraphContext.drawLine(exes[0], whys[0], exes[1], whys[1]);
								} else {
									Sys.this.oudPlotterImgBufGraphContext.setColor(Color.black);
									Sys.this.oudPlotterImgBufGraphContext.fillPolygon(exes, whys, 4);
								}
							}
							// put some info on the geometry
							if (DesignCard.this.displ == 3) {
								if (DesignCard.this.foiltype <= FOILTYPE_PLATE) {
									Sys.this.oudPlotterImgBufGraphContext.setColor(Color.red);
									exes[1] = (int) (DesignCard.this.plscale * DesignCard.this.factp
											* (DesignCard.this.xpl[0][inmax]
													- 4.0 * Math.cos(DesignCard.this.convdr * DesignCard.this.alfval)))
											+ DesignCard.this.xtp;
									whys[1] = (int) (DesignCard.this.plscale * DesignCard.this.factp
											* (-DesignCard.this.ypl[0][inmax]
													- 4.0 * Math.sin(DesignCard.this.convdr * DesignCard.this.alfval)))
											+ DesignCard.this.ytp;
									Sys.this.oudPlotterImgBufGraphContext.drawLine(exes[1], whys[1], camx[6], camy[6]);
									for (i = 6; i <= DesignCard.this.npt2 - 2; ++i) {
										Sys.this.oudPlotterImgBufGraphContext.drawLine(camx[i], camy[i], camx[i + 1],
												camy[i + 1]);
									}
									Sys.this.oudPlotterImgBufGraphContext.setColor(Color.white);
									Sys.this.oudPlotterImgBufGraphContext.fillRect(160, 615, 180, 20);
									Sys.this.oudPlotterImgBufGraphContext.setColor(Color.red);
									Sys.this.oudPlotterImgBufGraphContext.drawString("Mean Camber Line", 175, 630);
								}
								if (DesignCard.this.foiltype >= FOILTYPE_CYLINDER) {
									Sys.this.oudPlotterImgBufGraphContext.setColor(Color.red);
									exes[0] = (int) (DesignCard.this.plscale * DesignCard.this.factp
											* DesignCard.this.xpl[0][1]) + DesignCard.this.xtp;
									whys[0] = (int) (DesignCard.this.plscale * DesignCard.this.factp
											* -DesignCard.this.ypl[0][1]) + DesignCard.this.ytp;
									exes[1] = (int) (DesignCard.this.plscale * DesignCard.this.factp
											* DesignCard.this.xpl[0][DesignCard.this.npt2]) + DesignCard.this.xtp;
									whys[1] = (int) (DesignCard.this.plscale * DesignCard.this.factp
											* -DesignCard.this.ypl[0][DesignCard.this.npt2]) + DesignCard.this.ytp;
									Sys.this.oudPlotterImgBufGraphContext.drawLine(exes[0], whys[0], exes[1], whys[1]);
									Sys.this.oudPlotterImgBufGraphContext.drawString(Sys.DIAMETER, exes[0] + 20,
											whys[0] + 20);
								}
							}
							// spin the cylinder and ball
							if (DesignCard.this.foiltype >= FOILTYPE_CYLINDER) {
								exes[0] = (int) (DesignCard.this.plscale * DesignCard.this.factp * (.5
										* (DesignCard.this.xpl[0][1] + DesignCard.this.xpl[0][DesignCard.this.npt2])
										+ DesignCard.this.rval
												* Math.cos(DesignCard.this.convdr * (DesignCard.this.plthg[1] + 180.))))
										+ DesignCard.this.xtp;
								whys[0] = (int) (DesignCard.this.plscale * DesignCard.this.factp
										* (-DesignCard.this.ypl[0][1] + DesignCard.this.rval
												* Math.sin(DesignCard.this.convdr * (DesignCard.this.plthg[1] + 180.))))
										+ DesignCard.this.ytp;
								exes[1] = (int) (DesignCard.this.plscale * DesignCard.this.factp * (.5
										* (DesignCard.this.xpl[0][1] + DesignCard.this.xpl[0][DesignCard.this.npt2])
										+ DesignCard.this.rval
												* Math.cos(DesignCard.this.convdr * DesignCard.this.plthg[1])))
										+ DesignCard.this.xtp;
								whys[1] = (int) (DesignCard.this.plscale * DesignCard.this.factp
										* (-DesignCard.this.ypl[0][1] + DesignCard.this.rval
												* Math.sin(DesignCard.this.convdr * DesignCard.this.plthg[1])))
										+ DesignCard.this.ytp;
								Sys.this.oudPlotterImgBufGraphContext.setColor(Color.red);
								Sys.this.oudPlotterImgBufGraphContext.drawLine(exes[0], whys[0], exes[1], whys[1]);
							}
							Sys.this.oudPlotterImgBufGraphContext.setColor(Color.white);
							Sys.this.oudPlotterImgBufGraphContext.fillRect(5, 5, 60, 20);
							Sys.this.oudPlotterImgBufGraphContext.setColor(Color.black);
							Sys.this.oudPlotterImgBufGraphContext.drawString("Layout", 10, 20);

							Sys.this.oudPlotterImgBufGraphContext.setColor(Color.white);
							Sys.this.oudPlotterImgBufGraphContext.fillRect(235, 10, 120, 60);
							Sys.this.oudPlotterImgBufGraphContext.setColor(Color.red);
							Sys.this.oudPlotterImgBufGraphContext.drawString("  Double", 240, 25);
							Sys.this.oudPlotterImgBufGraphContext.setColor(Color.blue);
							Sys.this.oudPlotterImgBufGraphContext.drawString("Scale =", 240, 45);
							Sys.this.oudPlotterImgBufGraphContext
									.drawString(String.valueOf(Sys.filter3(1.0 / DesignCard.this.plscale)), 300, 45);
							if (DesignCard.this.lunits == UNITS_ENGLISH) {
								Sys.this.oudPlotterImgBufGraphContext.drawString("ft", 340, 45);
							}
							if (DesignCard.this.lunits == UNITS_METERIC) {
								Sys.this.oudPlotterImgBufGraphContext.drawString("dm", 340, 45);
							}
							Sys.this.oudPlotterImgBufGraphContext.setColor(Color.red);
							Sys.this.oudPlotterImgBufGraphContext.drawString("  Half", 240, 65);

							g.drawImage(Sys.this.oudPlotterImageBuffer, 0, 0, this);
						}
					}

					/**
					 * (non-Javadoc).
					 *
					 * @see java.lang.Runnable#run()
					 */
					@Override
					public void run() {
						int timer;

						timer = 100;
						while (true) {
							try {
								Thread.sleep(timer);
							} catch (final InterruptedException e) {
							}
							DesignCard.this.oud.plotter.repaint();
						}
					}

					/** Start. */
					public void start() {
						if (this.run2 == null) {
							this.run2 = new Thread(this);
							this.run2.start();
						}
						Sys.logger.info("Oud Plotter Started");
					}

					/**
					 * (non-Javadoc).
					 *
					 * @param g
					 *            the g
					 * @see java.awt.Canvas#update(java.awt.Graphics)
					 */
					@Override
					public void update(final Graphics g) {
						DesignCard.this.oud.plotter.paint(g);
					}
				} // Plt

				/** The Constant serialVersionUID. */
				private static final long serialVersionUID = 1L;

				/** The outerparent. */
				Sys outerparent;

				/** The plt. */
				OudPlotter plotter;

				/**
				 * Instantiates a new oud.
				 *
				 * @param target
				 *            the target
				 */
				Oud(final Sys target) {
					this.outerparent = target;
					DesignCard.this.layout = new CardLayout();
					this.setLayout(DesignCard.this.layout);

					this.plotter = new OudPlotter(this.outerparent);

					this.add(MainPanel.FIRST_CARD, this.plotter);
				}
			} // Oud

			/** The Class Solved. */
			class SolveDesign {

				/** Instantiates a new solved. */
				SolveDesign() {

				}

				/** Gen flow. */
				public void genFlow() { // generate flowfield
					double rnew, thet, psv, fxg;
					int k, index;
					/* all lines of flow except stagnation line */
					for (k = 1; k <= DesignCard.this.nlnc; ++k) {
						psv = -.5 * (DesignCard.this.nln2 - 1) + .5 * (k - 1);
						fxg = DesignCard.this.xflow;
						for (index = 1; index <= DesignCard.this.nptc; ++index) {
							DesignCard.this.solved.getPoints(fxg, psv);
							DesignCard.this.xg[k][index] = DesignCard.this.lxgt;
							DesignCard.this.yg[k][index] = DesignCard.this.lygt;
							DesignCard.this.rg[k][index] = DesignCard.this.lrgt;
							DesignCard.this.thg[k][index] = DesignCard.this.lthgt;
							DesignCard.this.xm[k][index] = DesignCard.this.lxmt;
							DesignCard.this.ym[k][index] = DesignCard.this.lymt;
							if (DesignCard.this.anflag == 1) { // stall model
								if (DesignCard.this.alfval > 10.0 && psv > 0.0) {
									if (DesignCard.this.xm[k][index] > 0.0) {
										DesignCard.this.ym[k][index] = DesignCard.this.ym[k][index - 1];
									}
								}
								if (DesignCard.this.alfval < -10.0 && psv < 0.0) {
									if (DesignCard.this.xm[k][index] > 0.0) {
										DesignCard.this.ym[k][index] = DesignCard.this.ym[k][index - 1];
									}
								}
							}
							DesignCard.this.solved.getVel(DesignCard.this.lrg, DesignCard.this.lthg);
							fxg = fxg + DesignCard.this.vxdir * DesignCard.this.deltb;
							DesignCard.this.xgc[k][index] = DesignCard.this.lxgtc;
							DesignCard.this.ygc[k][index] = DesignCard.this.lygtc;
						}
					}
					/* stagnation line */
					k = DesignCard.this.nln2;
					psv = 0.0;
					/* incoming flow */
					for (index = 1; index <= DesignCard.this.npt2; ++index) {
						rnew = 10.0 - (10.0 - DesignCard.this.rval)
								* Math.sin(DesignCard.this.pid2 * (index - 1) / (DesignCard.this.npt2 - 1));
						thet = Math.asin(.999 * (psv - DesignCard.this.gamval * Math.log(rnew / DesignCard.this.rval))
								/ (rnew - DesignCard.this.rval * DesignCard.this.rval / rnew));
						fxg = -rnew * Math.cos(thet);
						DesignCard.this.solved.getPoints(fxg, psv);
						DesignCard.this.xg[k][index] = DesignCard.this.lxgt;
						DesignCard.this.yg[k][index] = DesignCard.this.lygt;
						DesignCard.this.rg[k][index] = DesignCard.this.lrgt;
						DesignCard.this.thg[k][index] = DesignCard.this.lthgt;
						DesignCard.this.xm[k][index] = DesignCard.this.lxmt;
						DesignCard.this.ym[k][index] = DesignCard.this.lymt;
						DesignCard.this.xgc[k][index] = DesignCard.this.lxgtc;
						DesignCard.this.ygc[k][index] = DesignCard.this.lygtc;
					}
					/* downstream flow */
					for (index = 1; index <= DesignCard.this.npt2; ++index) {
						rnew = 10.0 + .01 - (10.0 - DesignCard.this.rval)
								* Math.cos(DesignCard.this.pid2 * (index - 1) / (DesignCard.this.npt2 - 1));
						thet = Math.asin(.999 * (psv - DesignCard.this.gamval * Math.log(rnew / DesignCard.this.rval))
								/ (rnew - DesignCard.this.rval * DesignCard.this.rval / rnew));
						fxg = rnew * Math.cos(thet);
						DesignCard.this.solved.getPoints(fxg, psv);
						DesignCard.this.xg[k][DesignCard.this.npt2 + index] = DesignCard.this.lxgt;
						DesignCard.this.yg[k][DesignCard.this.npt2 + index] = DesignCard.this.lygt;
						DesignCard.this.rg[k][DesignCard.this.npt2 + index] = DesignCard.this.lrgt;
						DesignCard.this.thg[k][DesignCard.this.npt2 + index] = DesignCard.this.lthgt;
						DesignCard.this.xm[k][DesignCard.this.npt2 + index] = DesignCard.this.lxmt;
						DesignCard.this.ym[k][DesignCard.this.npt2 + index] = DesignCard.this.lymt;
						DesignCard.this.xgc[k][index] = DesignCard.this.lxgtc;
						DesignCard.this.ygc[k][index] = DesignCard.this.lygtc;
					}
					/* stagnation point */
					DesignCard.this.xg[k][DesignCard.this.npt2] = DesignCard.this.xcval;
					DesignCard.this.yg[k][DesignCard.this.npt2] = DesignCard.this.ycval;
					DesignCard.this.rg[k][DesignCard.this.npt2] = Math
							.sqrt(DesignCard.this.xcval * DesignCard.this.xcval
									+ DesignCard.this.ycval * DesignCard.this.ycval);
					DesignCard.this.thg[k][DesignCard.this.npt2] = Math.atan2(DesignCard.this.ycval,
							DesignCard.this.xcval) / DesignCard.this.convdr;
					DesignCard.this.xm[k][DesignCard.this.npt2] = (DesignCard.this.xm[k][DesignCard.this.npt2 + 1]
							+ DesignCard.this.xm[k][DesignCard.this.npt2 - 1]) / 2.0;
					DesignCard.this.ym[k][DesignCard.this.npt2] = (DesignCard.this.ym[0][DesignCard.this.nptc / 4 + 1]
							+ DesignCard.this.ym[0][DesignCard.this.nptc / 4 * 3 + 1]) / 2.0;
					/* compute lift coefficient */
					DesignCard.this.leg = DesignCard.this.xcval - Math.sqrt(DesignCard.this.rval * DesignCard.this.rval
							- DesignCard.this.ycval * DesignCard.this.ycval);
					DesignCard.this.teg = DesignCard.this.xcval + Math.sqrt(DesignCard.this.rval * DesignCard.this.rval
							- DesignCard.this.ycval * DesignCard.this.ycval);
					DesignCard.this.lem = DesignCard.this.leg + 1.0 / DesignCard.this.leg;
					DesignCard.this.tem = DesignCard.this.teg + 1.0 / DesignCard.this.teg;
					DesignCard.this.chrd = DesignCard.this.tem - DesignCard.this.lem;
					DesignCard.this.clift = DesignCard.this.gamval * 4.0 * Sys.PI / DesignCard.this.chrd;

					return;
				}

				/**
				 * Gets the circulation.
				 *
				 * @return the circulation
				 */
				public void getCirculation() { // circulation from Kutta condition
					DesignCard.this.xcval = 0.0;
					switch (DesignCard.this.foiltype) {
					case FOILTYPE_JUOKOWSKI: { /* Juokowski geometry */
						juokowskiGeometry();
						break;
					}
					case FOILTYPE_ELLIPTICAL: { /* Elliptical geometry */
						ellipticalGeometry();
						break;
					}
					case FOILTYPE_PLATE: { /* Plate geometry */
						plateGeometry();
						break;
					}
					case FOILTYPE_CYLINDER: { /* get circulation for rotating cylinder */
						cylinderGeometry();
						break;
					}
					case FOILTYPE_BALL: { /* get circulation for rotating ball */
						cylinderGeometry();
						break;
					}
					}

					if (DesignCard.this.foiltype <= 3 && DesignCard.this.anflag == 2) {
						DesignCard.this.gamval = 0.0;
					}

					return;
				}

				/**
				 * Cylinder geometry.
				 */
				private void cylinderGeometry() {
					DesignCard.this.rval = DesignCard.this.radius / DesignCard.this.lconv;
					DesignCard.this.gamval = 4.0 * Sys.PI * Sys.PI * DesignCard.this.spin * DesignCard.this.rval
							* DesignCard.this.rval / (DesignCard.this.vfsd / DesignCard.this.vconv);
					DesignCard.this.gamval = DesignCard.this.gamval * DesignCard.this.spindr;
					DesignCard.this.ycval = .0001;
				}

				/**
				 * Plate geometry.
				 */
				private void plateGeometry() {
					double beta;
					DesignCard.this.ycval = DesignCard.this.camval / 2.0;
					DesignCard.this.rval = Math.sqrt(DesignCard.this.ycval * DesignCard.this.ycval + 1.0);
					beta = Math.asin(DesignCard.this.ycval / DesignCard.this.rval)
							/ DesignCard.this.convdr; /* Kutta condition */
					DesignCard.this.gamval = 2.0 * DesignCard.this.rval
							* Math.sin((DesignCard.this.alfval + beta) * DesignCard.this.convdr);
				}

				/**
				 * Elliptical geometry.
				 */
				private void ellipticalGeometry() {
					double beta;
					DesignCard.this.ycval = DesignCard.this.camval / 2.0;
					DesignCard.this.rval = DesignCard.this.thkval / 4.0
							+ Math.sqrt(DesignCard.this.thkval * DesignCard.this.thkval / 16.0
									+ DesignCard.this.ycval * DesignCard.this.ycval + 1.0);
					beta = Math.asin(DesignCard.this.ycval / DesignCard.this.rval)
							/ DesignCard.this.convdr; /* Kutta condition */
					DesignCard.this.gamval = 2.0 * DesignCard.this.rval
							* Math.sin((DesignCard.this.alfval + beta) * DesignCard.this.convdr);
				}

				/**
				 * Juokowski geometry.
				 */
				private void juokowskiGeometry() {
					double beta;
					DesignCard.this.ycval = DesignCard.this.camval / 2.0;
					DesignCard.this.rval = DesignCard.this.thkval / 4.0
							+ Math.sqrt(DesignCard.this.thkval * DesignCard.this.thkval / 16.0
									+ DesignCard.this.ycval * DesignCard.this.ycval + 1.0);
					DesignCard.this.xcval = 1.0 - Math.sqrt(DesignCard.this.rval * DesignCard.this.rval
							- DesignCard.this.ycval * DesignCard.this.ycval);
					beta = Math.asin(DesignCard.this.ycval / DesignCard.this.rval)
							/ DesignCard.this.convdr; /* Kutta condition */
					DesignCard.this.gamval = 2.0 * DesignCard.this.rval
							* Math.sin((DesignCard.this.alfval + beta) * DesignCard.this.convdr);
				}

				/**
				 * Gets the free stream.
				 *
				 * @return the free stream
				 */
				public void getFreeStream() { // free stream conditions
					double hite; /* MODS 19 Jan 00 whole routine */
					double rgas;

					DesignCard.this.g0 = 32.2;
					rgas = 1718.; /* ft2/sec2 R */
					hite = DesignCard.this.alt / DesignCard.this.lconv;
					if (DesignCard.this.planet == PLANET_EARTH) { // Earth standard day
						if (hite <= 36152.) { // Troposphere
							DesignCard.this.ts0 = 518.6 - 3.56 * hite / 1000.;
							DesignCard.this.ps0 = 2116. * Math.pow(DesignCard.this.ts0 / 518.6, 5.256);
						}
						if (hite >= 36152. && hite <= 82345.) { // Stratosphere
							DesignCard.this.ts0 = 389.98;
							DesignCard.this.ps0 = 2116. * .2236 * Math.exp((36000. - hite) / (53.35 * 389.98));
						}
						if (hite >= 82345.) {
							DesignCard.this.ts0 = 389.98 + 1.645 * (hite - 82345) / 1000.;
							DesignCard.this.ps0 = 2116. * .02456 * Math.pow(DesignCard.this.ts0 / 389.98, -11.388);
						}
						DesignCard.this.temf = DesignCard.this.ts0 - 459.6;
						if (DesignCard.this.temf <= 0.0) {
							DesignCard.this.temf = 0.0;
						}
						/*
						 * Eq 1:6A Domasch - effect of humidity rlhum = 0.0 ; presm = ps0 * 29.92 /
						 * 2116. ; pvap = rlhum*(2.685+.00353*Math.pow(temf,2.245)); rho = (ps0 -
						 * .379*pvap)/(rgas * ts0) ;
						 */
						DesignCard.this.rho = DesignCard.this.ps0 / (rgas * DesignCard.this.ts0);
					}
					if (DesignCard.this.planet == PLANET_MARS) { // Mars - curve fit of orbiter data
						rgas = 1149.; /* ft2/sec2 R */
						if (hite <= 22960.) {
							DesignCard.this.ts0 = 434.02 - .548 * hite / 1000.;
							DesignCard.this.ps0 = 14.62 * Math.pow(2.71828, -.00003 * hite);
						}
						if (hite > 22960.) {
							DesignCard.this.ts0 = 449.36 - 1.217 * hite / 1000.;
							DesignCard.this.ps0 = 14.62 * Math.pow(2.71828, -.00003 * hite);
						}
						DesignCard.this.rho = DesignCard.this.ps0 / (rgas * DesignCard.this.ts0);
					}

					if (DesignCard.this.planet == PLANET_WATER) { // water -- constant density
						hite = -DesignCard.this.alt / DesignCard.this.lconv;
						DesignCard.this.ts0 = 520.;
						DesignCard.this.rho = 1.94;
						DesignCard.this.ps0 = 2116. - DesignCard.this.rho * DesignCard.this.g0 * hite;
					}

					if (DesignCard.this.planet == PLANET_AIR_TEMP) { // specify air temp and pressure
						DesignCard.this.rho = DesignCard.this.ps0 / (rgas * DesignCard.this.ts0);
					}

					if (DesignCard.this.planet == PLANET_FLUID_DENSITY) { // specify fluid density
						DesignCard.this.ps0 = 2116.;
					}

					DesignCard.this.q0 = .5 * DesignCard.this.rho * DesignCard.this.vfsd * DesignCard.this.vfsd
							/ (DesignCard.this.vconv * DesignCard.this.vconv);
					// DesignCard.this.pt0 = DesignCard.this.ps0 + DesignCard.this.q0;

					return;
				}

				/**
				 * Gets the geom.
				 *
				 * @return the geom
				 */
				public void getGeometry() { // geometry
					double thet, rdm, thtm;
					int index;

					for (index = 1; index <= DesignCard.this.nptc; ++index) {
						thet = (index - 1) * 360. / (DesignCard.this.nptc - 1);
						DesignCard.this.xg[0][index] = DesignCard.this.rval * Math.cos(DesignCard.this.convdr * thet)
								+ DesignCard.this.xcval;
						DesignCard.this.yg[0][index] = DesignCard.this.rval * Math.sin(DesignCard.this.convdr * thet)
								+ DesignCard.this.ycval;
						DesignCard.this.rg[0][index] = Math
								.sqrt(DesignCard.this.xg[0][index] * DesignCard.this.xg[0][index]
										+ DesignCard.this.yg[0][index] * DesignCard.this.yg[0][index]);
						DesignCard.this.thg[0][index] = Math.atan2(DesignCard.this.yg[0][index],
								DesignCard.this.xg[0][index]) / DesignCard.this.convdr;
						DesignCard.this.xm[0][index] = (DesignCard.this.rg[0][index]
								+ 1.0 / DesignCard.this.rg[0][index])
								* Math.cos(DesignCard.this.convdr * DesignCard.this.thg[0][index]);
						DesignCard.this.ym[0][index] = (DesignCard.this.rg[0][index]
								- 1.0 / DesignCard.this.rg[0][index])
								* Math.sin(DesignCard.this.convdr * DesignCard.this.thg[0][index]);
						rdm = Math.sqrt(DesignCard.this.xm[0][index] * DesignCard.this.xm[0][index]
								+ DesignCard.this.ym[0][index] * DesignCard.this.ym[0][index]);
						thtm = Math.atan2(DesignCard.this.ym[0][index], DesignCard.this.xm[0][index])
								/ DesignCard.this.convdr;
						DesignCard.this.xm[0][index] = rdm
								* Math.cos((thtm - DesignCard.this.alfval) * DesignCard.this.convdr);
						DesignCard.this.ym[0][index] = rdm
								* Math.sin((thtm - DesignCard.this.alfval) * DesignCard.this.convdr);
						this.getVel(DesignCard.this.rval, thet);
						DesignCard.this.plp[index] = (DesignCard.this.ps0 + DesignCard.this.pres * DesignCard.this.q0)
								/ 2116. * DesignCard.this.pconv;
						DesignCard.this.plv[index] = DesignCard.this.vel * DesignCard.this.vfsd;
						DesignCard.this.xgc[0][index] = DesignCard.this.rval * Math.cos(DesignCard.this.convdr * thet)
								+ DesignCard.this.xcval;
						DesignCard.this.ygc[0][index] = DesignCard.this.rval * Math.sin(DesignCard.this.convdr * thet)
								+ DesignCard.this.ycval;
					}

					DesignCard.this.xt1 = DesignCard.this.xt + DesignCard.this.spanfac;
					DesignCard.this.yt1 = DesignCard.this.yt - DesignCard.this.spanfac;
					DesignCard.this.xt2 = DesignCard.this.xt - DesignCard.this.spanfac;
					DesignCard.this.yt2 = DesignCard.this.yt + DesignCard.this.spanfac;

					return;
				}

				/**
				 * Gets the points.
				 *
				 * @param fxg
				 *            the fxg
				 * @param psv
				 *            the psv
				 * @return the points
				 */
				public void getPoints(final double fxg, final double psv) { // flow in x-psi
					double radm, thetm; /* MODS 20 Jul 99 whole routine */
					double fnew, ynew, yold, rfac, deriv;
					int iter;
					/* get variables in the generating plane */
					/* iterate to find value of yg */
					ynew = 10.0;
					yold = 10.0;
					if (psv < 0.0) {
						ynew = -10.0;
					}
					if (Math.abs(psv) < .001 && DesignCard.this.alfval < 0.0) {
						ynew = DesignCard.this.rval;
					}
					if (Math.abs(psv) < .001 && DesignCard.this.alfval >= 0.0) {
						ynew = -DesignCard.this.rval;
					}
					fnew = 0.1;
					iter = 1;
					while (Math.abs(fnew) >= .00001 && iter < 25) {
						++iter;
						rfac = fxg * fxg + ynew * ynew;
						if (rfac < DesignCard.this.rval * DesignCard.this.rval) {
							rfac = DesignCard.this.rval * DesignCard.this.rval + .01;
						}
						fnew = psv - ynew * (1.0 - DesignCard.this.rval * DesignCard.this.rval / rfac)
								- DesignCard.this.gamval * Math.log(Math.sqrt(rfac) / DesignCard.this.rval);
						deriv = -(1.0 - DesignCard.this.rval * DesignCard.this.rval / rfac)
								- 2.0 * ynew * ynew * DesignCard.this.rval * DesignCard.this.rval / (rfac * rfac)
								- DesignCard.this.gamval * ynew / rfac;
						yold = ynew;
						ynew = yold - .5 * fnew / deriv;
					}
					DesignCard.this.lyg = yold;
					/* rotate for angle of attack */
					DesignCard.this.lrg = Math.sqrt(fxg * fxg + DesignCard.this.lyg * DesignCard.this.lyg);
					DesignCard.this.lthg = Math.atan2(DesignCard.this.lyg, fxg) / DesignCard.this.convdr;
					DesignCard.this.lxgt = DesignCard.this.lrg
							* Math.cos(DesignCard.this.convdr * (DesignCard.this.lthg + DesignCard.this.alfval));
					DesignCard.this.lygt = DesignCard.this.lrg
							* Math.sin(DesignCard.this.convdr * (DesignCard.this.lthg + DesignCard.this.alfval));
					/* translate cylinder to generate airfoil */
					DesignCard.this.lxgtc = DesignCard.this.lxgt = DesignCard.this.lxgt + DesignCard.this.xcval;
					DesignCard.this.lygtc = DesignCard.this.lygt = DesignCard.this.lygt + DesignCard.this.ycval;
					DesignCard.this.lrgt = Math.sqrt(
							DesignCard.this.lxgt * DesignCard.this.lxgt + DesignCard.this.lygt * DesignCard.this.lygt);
					DesignCard.this.lthgt = Math.atan2(DesignCard.this.lygt, DesignCard.this.lxgt)
							/ DesignCard.this.convdr;
					/* Kutta-Joukowski mapping */
					DesignCard.this.lxm = (DesignCard.this.lrgt + 1.0 / DesignCard.this.lrgt)
							* Math.cos(DesignCard.this.convdr * DesignCard.this.lthgt);
					DesignCard.this.lym = (DesignCard.this.lrgt - 1.0 / DesignCard.this.lrgt)
							* Math.sin(DesignCard.this.convdr * DesignCard.this.lthgt);
					/* tranforms for view fixed with free stream */
					/* take out rotation for angle of attack mapped and cylinder */
					radm = Math.sqrt(
							DesignCard.this.lxm * DesignCard.this.lxm + DesignCard.this.lym * DesignCard.this.lym);
					thetm = Math.atan2(DesignCard.this.lym, DesignCard.this.lxm) / DesignCard.this.convdr;
					DesignCard.this.lxmt = radm * Math.cos(DesignCard.this.convdr * (thetm - DesignCard.this.alfval));
					DesignCard.this.lymt = radm * Math.sin(DesignCard.this.convdr * (thetm - DesignCard.this.alfval));
					DesignCard.this.lxgt = DesignCard.this.lxgt - DesignCard.this.xcval;
					DesignCard.this.lygt = DesignCard.this.lygt - DesignCard.this.ycval;
					DesignCard.this.lrgt = Math.sqrt(
							DesignCard.this.lxgt * DesignCard.this.lxgt + DesignCard.this.lygt * DesignCard.this.lygt);
					DesignCard.this.lthgt = Math.atan2(DesignCard.this.lygt, DesignCard.this.lxgt)
							/ DesignCard.this.convdr;
					DesignCard.this.lxgt = DesignCard.this.lrgt
							* Math.cos((DesignCard.this.lthgt - DesignCard.this.alfval) * DesignCard.this.convdr);
					DesignCard.this.lygt = DesignCard.this.lrgt
							* Math.sin((DesignCard.this.lthgt - DesignCard.this.alfval) * DesignCard.this.convdr);

					return;
				}

				/**
				 * Gets the probe.
				 *
				 * @return the probe
				 */
				public void getProbe() { /* all of the information needed for the probe */
					double prxg;
					int index;
					/* get variables in the generating plane */
					if (Math.abs(DesignCard.this.ypval) < .01) {
						DesignCard.this.ypval = .05;
					}
					DesignCard.this.solved.getPoints(DesignCard.this.xpval, DesignCard.this.ypval);

					DesignCard.this.solved.getVel(DesignCard.this.lrg, DesignCard.this.lthg);
					DesignCard.this.loadProbe();

					/* smoke */
					if (DesignCard.this.pboflag == PROBLE_SMOKE) {
						prxg = DesignCard.this.xpval;
						for (index = 1; index <= DesignCard.this.nptc; ++index) {
							DesignCard.this.solved.getPoints(prxg, DesignCard.this.ypval);
							DesignCard.this.xg[19][index] = DesignCard.this.lxgt;
							DesignCard.this.yg[19][index] = DesignCard.this.lygt;
							DesignCard.this.rg[19][index] = DesignCard.this.lrgt;
							DesignCard.this.thg[19][index] = DesignCard.this.lthgt;
							DesignCard.this.xm[19][index] = DesignCard.this.lxmt;
							DesignCard.this.ym[19][index] = DesignCard.this.lymt;
							if (DesignCard.this.anflag == 1) { // stall model
								if (DesignCard.this.xpval > 0.0) {
									if (DesignCard.this.alfval > 10.0 && DesignCard.this.ypval > 0.0) {
										DesignCard.this.ym[19][index] = DesignCard.this.ym[19][1];
									}
									if (DesignCard.this.alfval < -10.0 && DesignCard.this.ypval < 0.0) {
										DesignCard.this.ym[19][index] = DesignCard.this.ym[19][1];
									}
								}
								if (DesignCard.this.xpval < 0.0) {
									if (DesignCard.this.alfval > 10.0 && DesignCard.this.ypval > 0.0) {
										if (DesignCard.this.xm[19][index] > 0.0) {
											DesignCard.this.ym[19][index] = DesignCard.this.ym[19][index - 1];
										}
									}
									if (DesignCard.this.alfval < -10.0 && DesignCard.this.ypval < 0.0) {
										if (DesignCard.this.xm[19][index] > 0.0) {
											DesignCard.this.ym[19][index] = DesignCard.this.ym[19][index - 1];
										}
									}
								}
							}
							DesignCard.this.solved.getVel(DesignCard.this.lrg, DesignCard.this.lthg);
							prxg = prxg + DesignCard.this.vxdir * DesignCard.this.deltb;
						}
					}
					return;
				}

				/**
				 * Gets the vel.
				 *
				 * @param rad
				 *            the rad
				 * @param theta
				 *            the theta
				 * @return the vel
				 */
				public void getVel(double rad, final double theta) { // velocity and pressure
					double ur, uth, jake1, jake2, jakesq;
					double xloc, yloc, thrad, alfrad;

					thrad = DesignCard.this.convdr * theta;
					alfrad = DesignCard.this.convdr * DesignCard.this.alfval;
					/* get x, y location in cylinder plane */
					xloc = rad * Math.cos(thrad);
					yloc = rad * Math.sin(thrad);
					/* velocity in cylinder plane */
					ur = Math.cos(thrad - alfrad) * (1.0 - DesignCard.this.rval * DesignCard.this.rval / (rad * rad));
					uth = -Math.sin(thrad - alfrad) * (1.0 + DesignCard.this.rval * DesignCard.this.rval / (rad * rad))
							- DesignCard.this.gamval / rad;
					DesignCard.this.usq = ur * ur + uth * uth;
					DesignCard.this.vxdir = ur * Math.cos(thrad) - uth * Math.sin(thrad); // MODS 20 Jul 99
					/* translate to generate airfoil */
					xloc = xloc + DesignCard.this.xcval;
					yloc = yloc + DesignCard.this.ycval;
					/* compute new radius-theta */
					rad = Math.sqrt(xloc * xloc + yloc * yloc);
					thrad = Math.atan2(yloc, xloc);
					/* compute Joukowski Jacobian */
					jake1 = 1.0 - Math.cos(2.0 * thrad) / (rad * rad);
					jake2 = Math.sin(2.0 * thrad) / (rad * rad);
					jakesq = jake1 * jake1 + jake2 * jake2;
					if (Math.abs(jakesq) <= .01) {
						jakesq = .01; /* protection */
					}
					DesignCard.this.vsq = DesignCard.this.usq / jakesq;
					/* vel is velocity ratio - pres is coefficient (p-p0)/q0 */
					if (DesignCard.this.foiltype <= FOILTYPE_PLATE) {
						DesignCard.this.vel = Math.sqrt(DesignCard.this.vsq);
						DesignCard.this.pres = 1.0 - DesignCard.this.vsq;
					}
					if (DesignCard.this.foiltype >= FOILTYPE_CYLINDER) {
						DesignCard.this.vel = Math.sqrt(DesignCard.this.usq);
						DesignCard.this.pres = 1.0 - DesignCard.this.usq;
					}
					return;
				}

				/** Sets the defaults. */
				public void setDefaults() {

					Sys.this.counter = 1;
					DesignCard.this.dato = 0;
					DesignCard.this.datp = 0;
					DesignCard.this.plscale = 1.0;
					DesignCard.this.arcor = 0;
					DesignCard.this.planet = PLANET_EARTH;
					DesignCard.this.lunits = 0;
					DesignCard.this.lftout = 0;
					DesignCard.this.inptopt = 0;
					DesignCard.this.outopt = 0;
					DesignCard.this.nlnc = 15;
					DesignCard.this.nln2 = DesignCard.this.nlnc / 2 + 1;
					DesignCard.this.nptc = 37;
					DesignCard.this.npt2 = DesignCard.this.nptc / 2 + 1;
					DesignCard.this.deltb = .5;
					DesignCard.this.foiltype = FOILTYPE_JUOKOWSKI;
					DesignCard.this.flflag = 1;
					DesignCard.this.thkval = .5;
					DesignCard.this.thkinpt = 12.5; /* MODS 10 SEP 99 */
					DesignCard.this.camval = 0.0;
					DesignCard.this.caminpt = 0.0;
					DesignCard.this.alfval = 0.0;
					DesignCard.this.gamval = 0.0;
					DesignCard.this.radius = 1.0;
					DesignCard.this.spin = 0.0;
					DesignCard.this.spindr = 1.0;
					DesignCard.this.rval = 1.0;
					DesignCard.this.ycval = 0.0;
					DesignCard.this.xcval = 0.0;
					DesignCard.this.displ = 3;
					DesignCard.this.viewflg = WALL_VIEW_TRANSPARENT;
					DesignCard.this.dispp = 0;
					DesignCard.this.dout = 0;
					DesignCard.this.stfact = 1.0;

					DesignCard.this.xpval = 2.1;
					DesignCard.this.ypval = -.5;
					DesignCard.this.pboflag = PROBE_OFF;
					DesignCard.this.xflow = -10.0; /* MODS 20 Jul 99 */

					DesignCard.this.pconv = 14.7;
					DesignCard.this.fconv = 1.0;
					DesignCard.this.vconv = .6818;
					DesignCard.this.vfsd = 100.;
					DesignCard.this.vmax = 250.;
					DesignCard.this.lconv = 1.0;
					DesignCard.this.graphconv = 1.0;

					DesignCard.this.alt = 0.0;
					DesignCard.this.chrdold = DesignCard.this.chord = 1.0;
					DesignCard.this.aspr = 3.28084;
					DesignCard.this.arold = DesignCard.this.area = 100.0;

					DesignCard.this.xt = 200;
					DesignCard.this.yt = 165;
					DesignCard.this.fact = 30.0;
					DesignCard.this.sldloc = 155;
					DesignCard.this.xtp = 180;
					DesignCard.this.ytp = 565;
					DesignCard.this.factp = 25.0;
					DesignCard.this.spanfac = (int) (2.0 * DesignCard.this.fact * DesignCard.this.aspr * .3535);
					DesignCard.this.xt1 = DesignCard.this.xt + DesignCard.this.spanfac;
					DesignCard.this.yt1 = DesignCard.this.yt - DesignCard.this.spanfac;
					DesignCard.this.xt2 = DesignCard.this.xt - DesignCard.this.spanfac;
					DesignCard.this.yt2 = DesignCard.this.yt + DesignCard.this.spanfac;
					DesignCard.this.plthg[1] = 0.0;

					DesignCard.this.probflag = 2;
					DesignCard.this.anflag = 0;
					DesignCard.this.vmn = 0.0;
					DesignCard.this.vmx = 250.0;
					DesignCard.this.camn = -25.0;
					DesignCard.this.camx = 25.0;
					DesignCard.this.thkmn = 1.0;
					DesignCard.this.thkmx = 26.0;
					DesignCard.this.chrdmn = 5. / (2.54 * 12);
					DesignCard.this.chrdmx = 3.281;
					DesignCard.this.spinmn = -1500.0;
					DesignCard.this.spinmx = 1500.0;
					DesignCard.this.radmn = .05;
					DesignCard.this.radmx = 5.0;

					DesignCard.this.laby = Sys.PRESS;
					DesignCard.this.labyu = Sys.PSI;
					DesignCard.this.labx = Sys.X2;
					DesignCard.this.labxu = Sys.CHORD3;
					DesignCard.this.iprint = 0;

					return;
				}
			} // end Solved

			/** The Constant serialVersionUID. */
			private static final long serialVersionUID = 1L;

			/** The arcor. */
			private int arcor;

			/** The thkmx. */
			private double camn;

			/** The thkmn. */
			private double thkmn;

			/** The camx. */
			private double camx;

			/** The thkmx. */
			private double thkmx;

			/** The spnold. */
			private double chord;

			/** The aspr. */
			private double aspr;

			/** The arold. */
			private double arold;

			/** The chrdold. */
			private double chrdold;

			/** The spnold. */
			private double spnold;

			/** The armx. */
			private double chrdmn;

			/** The chrdmx. */
			private double chrdmx;

			/** The convdr. */
			private double convdr = Sys.PI / 180.;

			/** The xflow. */
			private double deltb;

			/** The xflow. */
			private double xflow; /* MODS 20 Jul 99 */

			/** The vfsd. */
			private double vfsd;

			/** The spin. */
			private double spin;

			/** The spindr. */
			private double spindr;

			/** The radius. */
			private double radius;

			/** The sldloc. */
			int displ;

			/** The viewflg. */
			int viewflg;

			/** The dispp. */
			int dispp;

			/** The dout. */
			int dout;

			/** The antim. */
			int antim;

			/** The ancol. */
			int ancol;

			/** The sldloc. */
			int sldloc;

			/** The factp. */
			/* plot & probe data */
			private double fact;

			/** The xpval. */
			private double xpval;

			/** The ypval. */
			private double ypval;

			/** The factp. */
			private double factp;

			/** The planet. */
			int foiltype;

			/** The flflag. */
			int flflag;

			/** The lunits. */
			int lunits;

			/** The lftout. */
			int lftout;

			/** The planet. */
			int planet;

			/** The presm. */
			private double g0;

			/** The q 0. */
			private double q0;

			/** The ps 0. */
			private double ps0;

			/** The ts 0. */
			private double ts0;

			/** The rho. */
			private double rho;

			/** The temf. */
			private double temf;

			/** The ind. */
			Ind ind;

			/** The datp. */
			int inptopt;

			/** The outopt. */
			int outopt;

			/** The iprint. */
			int iprint;

			/** The dato. */
			int dato;

			/** The datp. */
			int datp;

			/** The labyu. */
			String labx;

			/** The labxu. */
			String labxu;

			/** The laby. */
			String laby;

			/** The labyu. */
			String labyu;

			/** The layplt. */
			CardLayout layin;

			/** The layout. */
			CardLayout layout;

			/** The layplt. */
			CardLayout layplt;

			/** The tem. */
			private double leg;

			/** The teg. */
			private double teg;

			/** The lem. */
			private double lem;

			/** The tem. */
			private double tem;

			/** The nond. */
			int lflag;

			/** The gflag. */
			int gflag;

			/** The nond. */
			int nond;

			/** The ntr. */
			int lines;

			/** The nord. */
			int nord;

			/** The nabs. */
			int nabs;

			/** The ntr. */
			int ntr;

			/** The vxdir. */
			private double lxm;

			/** The lym. */
			private double lym;

			/** The lxmt. */
			private double lxmt;

			/** The lymt. */
			private double lymt;

			/** The vxdir. */
			private double vxdir;/* MOD 20 Jul */

			/** The lygtc. */
			private double lyg;

			/** The lrg. */
			private double lrg;

			/** The lthg. */
			private double lthg;

			/** The lxgt. */
			private double lxgt;

			/** The lygt. */
			private double lygt;

			/** The lrgt. */
			private double lrgt;

			/** The lthgt. */
			private double lthgt;

			/** The lxgtc. */
			private double lxgtc;

			/** The lygtc. */
			private double lygtc;

			/** The anflag. */
			int nptc;

			/** The npt 2. */
			int npt2;

			/** The nlnc. */
			int nlnc;

			/** The nln 2. */
			int nln2;

			/** The rdflag. */
			int rdflag;

			/** The browflag. */
			int browflag;

			/** The probflag. */
			int probflag;

			/** The anflag. */
			int anflag;

			/** The oud. */
			Oud oud;

			/** The outerparent. */
			Sys outerparent;

			/** The ytp. */
			int pboflag;

			/** The xt. */
			int xt;

			/** The yt. */
			int yt;

			/** The ntikx. */
			int ntikx;

			/** The ntiky. */
			int ntiky;

			/** The npt. */
			int npt;

			/** The xtp. */
			int xtp;

			/** The ytp. */
			int ytp;

			/** The graphconv. */
			private double pconv;

			/** The lconv. */
			private double lconv;

			/** The fconv. */
			private double fconv;

			/** The graphconv. */
			private double graphconv;

			/** The pid 2. */
			private double pid2 = Sys.PI / 2.0;

			/** The plp. */
			private double plp[] = new double[40];

			/** The plscale. */
			private double plscale;

			/** The plthg. */
			private double plthg[] = new double[2];

			/** The plv. */
			private double plv[] = new double[40];

			/** The spinmx. */
			private double radmn;

			/** The spinmn. */
			private double spinmn;

			/** The radmx. */
			private double radmx;

			/** The spinmx. */
			private double spinmx;

			/** The rg. */
			private double rg[][] = new double[20][40];

			/** The clift. */
			private double rval;

			/** The ycval. */
			private double ycval;

			/** The xcval. */
			private double xcval;

			/** The gamval. */
			private double gamval;

			/** The alfval. */
			private double alfval;

			/** The thkval. */
			private double thkval;

			/** The camval. */
			private double camval;

			/** The chrd. */
			private double chrd;

			/** The clift. */
			private double clift;

			/** The solved. */
			SolveDesign solved;

			/** The span. */
			private double span = 3.281; // Hold wing span constant

			/** The stfact. */
			private double stfact;

			/** The thg. */
			private double thg[][] = new double[20][40];

			/** The caminpt. */
			private double thkinpt;

			/** The caminpt. */
			private double caminpt; /* MODS 10 Sep 99 */

			/** The armin. */
			private double usq;

			/** The vsq. */
			private double vsq;

			/** The alt. */
			private double alt;

			/** The area. */
			private double area;

			/** The armax. */
			// private double armax;

			/** The armin. */
			// private double armin;

			/** The vmax. */
			private double vconv;

			/** The vmax. */
			private double vmax;

			/** The angr. */
			private double vel;

			/** The pres. */
			private double pres;

			/** The lift. */
			private double lift;

			/** The side. */
			// private double side;

			/** The omega. */
			// private double omega;

			/** The radcrv. */
			// private double radcrv;

			/** The relsy. */
			// private double relsy;

			/** The angr. */
			// private double angr;

			/** The angmx. */
			/* units data */
			private double vmn;

			/** The almn. */
			// private double almn;

			/** The angmn. */
			// private double angmn;

			/** The vmx. */
			private double vmx;

			/** The almx. */
			// private double almx;

			/** The angmx. */
			// private double angmx;

			/** The xg. */
			private double xg[][] = new double[20][40];

			/** The xgc. */
			private double xgc[][] = new double[20][40];

			/** The xm. */
			private double xm[][] = new double[20][40];

			/** The xpl. */
			private double xpl[][] = new double[20][40];

			/** The xplg. */
			private double xplg[][] = new double[20][40];

			/** The spanfac. */
			int xt1;

			/** The yt 1. */
			int yt1;

			/** The xt 2. */
			int xt2;

			/** The yt 2. */
			int yt2;

			/** The spanfac. */
			int spanfac;

			/** The yg. */
			private double yg[][] = new double[20][40];

			/** The ygc. */
			private double ygc[][] = new double[20][40];

			/** The ym. */
			private double ym[][] = new double[20][40];

			/** The ypl. */
			private double ypl[][] = new double[20][40];

			/** The yplg. */
			private double yplg[][] = new double[20][40];

			/**
			 * Instantiates a new dsys.
			 *
			 * @param target
			 *            the target
			 */
			DesignCard(final Sys target) {
				Sys.logger.info("Create Design Card");
				this.setName("Design Card");
				this.outerparent = target;
				this.setLayout(new GridLayout(1, 2, 5, 5));

				this.solved = new SolveDesign();

				this.solved.setDefaults();

				this.ind = new Ind(this.outerparent);
				this.oud = new Oud(this.outerparent);

				this.add(this.ind);
				this.add(this.oud);

				this.solved.getFreeStream();
				this.computeFlow();
				this.ind.view.start();
				this.oud.plotter.start();
			}

			/** Compute flow. */
			public void computeFlow() {

				this.solved.getFreeStream();

				if (this.flflag == 1) {
					this.solved.getCirculation(); /* get circulation */
				}
				this.solved.getGeometry(); /* get geometry */
				this.solved.genFlow();

				this.solved.getProbe();

				this.loadOut();

				this.loadPlot();
			}

			/**
			 * Gets the clplot.
			 *
			 * @param camb
			 *            the camb
			 * @param thic
			 *            the thic
			 * @param angl
			 *            the angl
			 * @return the clplot
			 */
			public double getClplot(final double camb, final double thic, final double angl) {
				double beta;
				double xc;
				double yc;
				double rc;
				double gamc;
				double lec;
				double tec;
				double lecm;
				double tecm;
				double crdc;
				double number;

				xc = 0.0;
				yc = camb / 2.0;
				rc = thic / 4.0 + Math.sqrt(thic * thic / 16.0 + yc * yc + 1.0);
				xc = 1.0 - Math.sqrt(rc * rc - yc * yc);
				beta = Math.asin(yc / rc) / this.convdr; /* Kutta condition */
				gamc = 2.0 * rc * Math.sin((angl + beta) * this.convdr);
				if (this.foiltype <= FOILTYPE_PLATE && this.anflag == 2) {
					gamc = 0.0;
				}
				lec = xc - Math.sqrt(rc * rc - yc * yc);
				tec = xc + Math.sqrt(rc * rc - yc * yc);
				lecm = lec + 1.0 / lec;
				tecm = tec + 1.0 / tec;
				crdc = tecm - lecm;
				// stall model 1
				this.stfact = 1.0;
				if (this.anflag == 1) {
					if (angl > 10.0) {
						this.stfact = .5 + .1 * angl - .005 * angl * angl;
					}
					if (angl < -10.0) {
						this.stfact = .5 - .1 * angl - .005 * angl * angl;
					}
				}

				number = this.stfact * gamc * 4.0 * Sys.PI / crdc;

				if (this.arcor == 1) { // correction for low aspect ratio
					number = number / (1.0 + number / (3.14159 * this.aspr));
				}

				return number;
			}

			/** Load input. */
			public void loadInput() { // load the input panels
				int i1;
				int i4;
				int i6;
				double v1;
				double v2;
				double v3;
				double v4;
				double v6;
				float fl1;
				float fl2;
				float fl3;
				float fl4;
				float fl6;
				// dimensional
				if (this.lunits == UNITS_ENGLISH) {
					this.ind.in.shp.upr.inl.l3.setText("Chord-ft");
					this.ind.in.shp.lwr.dwn.l3.setText("Area-sq ft");
					this.ind.in.cyl.inl.l2.setText(Sys.RADIUS_FT);
				}
				if (this.lunits == UNITS_METERIC) {
					this.ind.in.shp.upr.inl.l3.setText("Chord-m");
					this.ind.in.shp.lwr.dwn.l3.setText("Area-sq m");
					this.ind.in.cyl.inl.l2.setText("Radius m");
				}
				v1 = this.chord;
				this.chrdmn = 5. / (2.54 * 12) * this.lconv;
				this.chrdmx = 3.281 * this.lconv;
				v2 = this.span;
				v3 = this.area;
				// this.armn = this.armin * this.lconv * this.lconv;
				// this.armx = this.armax * this.lconv * this.lconv;
				v4 = this.vfsd;
				this.vmn = 0.0;
				this.vmx = this.vmax;
				// v5 = this.alt;
				// this.almn = 0.0;
				// this.almx = this.altmax * this.lconv;
				v6 = this.radius;
				this.radmn = .05 * this.lconv;
				this.radmx = 5.0 * this.lconv;
				this.aspr = this.span / this.chord;
				this.spanfac = (int) (2.0 * this.fact * this.aspr * .3535);

				fl1 = Sys.filter3(v1);
				fl2 = (float) v2;
				fl3 = (float) v3;
				fl4 = Sys.filter3(v4);
				fl6 = (float) v6;

				this.ind.in.shp.upr.inl.f3.setText(String.valueOf(fl1));
				this.ind.in.shp.lwr.dwn.o3.setText(String.valueOf(fl3));
				this.ind.in.cyl.inl.f2.setText(String.valueOf(fl6));

				i1 = (int) ((v1 - this.chrdmn) / (this.chrdmx - this.chrdmn) * 1000.);
				i4 = (int) ((v4 - this.vmn) / (this.vmx - this.vmn) * 1000.);
				i6 = (int) ((v6 - this.radmn) / (this.radmx - this.radmn) * 1000.);

				this.ind.in.shp.upr.inr.s3.setValue(i1);
				this.ind.in.cyl.inr.s2.setValue(i6);
				// non-dimensional
				v1 = this.caminpt;
				v2 = this.thkinpt;
				v3 = this.alfval;
				v4 = this.spin * 60.0;

				fl1 = (float) v1;
				fl2 = (float) v2;
				fl3 = (float) v3;
				fl4 = (float) v4;

				this.ind.in.shp.upr.inl.f1.setText(String.valueOf(fl1));
				this.ind.in.shp.upr.inl.f2.setText(String.valueOf(fl2));
				this.ind.in.cyl.inl.f1.setText(String.valueOf(fl4));

				i1 = (int) ((v1 - this.camn) / (this.camx - this.camn) * 1000.);
				i4 = (int) ((v4 - this.spinmn) / (this.spinmx - this.spinmn) * 1000.);
				this.ind.in.shp.upr.inr.s1.setValue(i1);
				this.ind.in.cyl.inr.s1.setValue(i4);

				this.computeFlow();
				return;
			}

			/** Load out. */
			public void loadOut() { // output routine
				if (this.lunits == UNITS_METERIC) {
				}

				if (this.foiltype <= FOILTYPE_PLATE) { // mapped airfoil
					// stall model
					this.stfact = 1.0;
					if (this.anflag == 1) {
						if (this.alfval > 10.0) {
							this.stfact = .5 + .1 * this.alfval - .005 * this.alfval * this.alfval;
						}
						if (this.alfval < -10.0) {
							this.stfact = .5 - .1 * this.alfval - .005 * this.alfval * this.alfval;
						}
						this.clift = this.clift * this.stfact;
					}

					if (this.arcor == 1) { // correction for low aspect ratio
						this.clift = this.clift / (1.0 + this.clift / (3.14159 * this.aspr));
					}

					if (this.lftout == 0) {
						this.lift = this.clift * this.q0 * this.area / this.lconv / this.lconv; /* lift in lbs */
						this.lift = this.lift * this.fconv;
					}
				}
				if (this.foiltype >= FOILTYPE_CYLINDER) { // cylinder and ball

					this.lift = this.rho * this.vfsd / this.vconv * this.gamval * this.vfsd / this.vconv * this.span
							/ this.lconv; // lift lbs
					if (this.foiltype == FOILTYPE_BALL) {
						this.lift = this.lift * Sys.PI / 2.0; // ball
					}
					this.lift = this.lift * this.fconv;
					if (this.lftout == 1) {
						this.clift = this.lift / this.fconv / (this.q0 * this.area / this.lconv / this.lconv);
					}
				}

				switch (this.lunits) {
				case 0: { /* English */
					this.ind.in.shp.lwr.dwn.o4.setText(String.valueOf(Sys.filter3(this.aspr)));
					break;
				}
				case 1: { /* Metric */
					this.ind.in.shp.lwr.dwn.o4.setText(String.valueOf(Sys.filter3(this.aspr)));
					break;
				}
				}
				// diagnostics
				// ind.in.shp.lwr.dwn.o1.setText(String.valueOf(filter3(fact))) ;
				// ind.in.shp.lwr.dwn.o2.setText(String.valueOf(filter0(sldloc))) ;
				return;
			}

			/** Load plot. */
			public void loadPlot() {
				double clref;
				int index;
				int ic;

				this.lines = 1;
				clref = this.getClplot(this.camval, this.thkval, this.alfval);
				if (Math.abs(clref) <= .001) {
					clref = .001; /* protection */
				}
				// load up the view image
				for (ic = 0; ic <= this.nlnc; ++ic) {
					for (index = 0; index <= this.nptc; ++index) {
						if (this.foiltype <= FOILTYPE_PLATE) {
							this.xpl[ic][index] = this.xm[ic][index];
							this.ypl[ic][index] = this.ym[ic][index];
						}
						if (this.foiltype >= FOILTYPE_CYLINDER) {
							this.xpl[ic][index] = this.xg[ic][index];
							this.ypl[ic][index] = this.yg[ic][index];
						}
					}
				}
				// load up the generating plane
				if (this.dispp == 25) {
					for (ic = 0; ic <= this.nlnc; ++ic) {
						for (index = 0; index <= this.nptc; ++index) {
							this.xplg[ic][index] = this.xgc[ic][index];
							this.yplg[ic][index] = this.ygc[ic][index];
						}
					}
				}
				// probe
				for (index = 0; index <= this.nptc; ++index) {
					if (this.foiltype <= FOILTYPE_PLATE) {
						this.xpl[19][index] = this.xm[19][index];
						this.ypl[19][index] = this.ym[19][index];
						// this.pxpl = this.pxm;
						// this.pypl = this.pym;
					}
					if (this.foiltype >= FOILTYPE_CYLINDER) {
						this.xpl[19][index] = this.xg[19][index];
						this.ypl[19][index] = this.yg[19][index];
						// this.pxpl = this.pxg;
						// this.pypl = this.pyg;
					}
				}
			}

			/** Load probe. */
			public void loadProbe() { // probe output routine

				// this.pbval = 0.0;
				if (this.pboflag == PROBE_VELOCITY) {
					// this.pbval = this.vel * this.vfsd; // velocity
				}
				if (this.pboflag == PROBE_PRESSURE) {
					// this.pbval = (this.ps0 + this.pres * this.q0) / 2116. * this.pconv; //
					// pressure
				}

				return;
			}

			/** Sets the units. */
			public void setUnits() { // Switching Units

				double ovs;
				double chords;
				double spans;
				double aros;
				double chos;
				double spos;
				double rads;
				double alts;
				double ares;

				alts = this.alt / this.lconv;
				chords = this.chord / this.lconv;
				spans = this.span / this.lconv;
				ares = this.area / this.lconv / this.lconv;
				aros = this.arold / this.lconv / this.lconv;
				chos = this.chrdold / this.lconv;
				spos = this.spnold / this.lconv;
				ovs = this.vfsd / this.vconv;
				rads = this.radius / this.lconv;

				switch (this.lunits) {
				case UNITS_ENGLISH: { /* English */
					englishUnits();
					break;
				}
				case UNITS_METERIC: { /* Metric */
					metricUnits();
					break;
				}
				}

				this.alt = alts * this.lconv;
				this.chord = chords * this.lconv;
				this.span = spans * this.lconv;
				this.area = ares * this.lconv * this.lconv;
				this.arold = aros * this.lconv * this.lconv;
				this.chrdold = chos * this.lconv;
				this.spnold = spos * this.lconv;
				this.vfsd = ovs * this.vconv;
				this.radius = rads * this.lconv;

				return;
			}

			/**
			 * Metric units.
			 */
			private void metricUnits() {
				this.lconv = .3048; /* meters */
				this.vconv = 1.097;
				this.vmax = 400.; /* km/hr */
				if (this.planet == 2) {
					this.vmax = 80.;
				}
				this.fconv = 4.448;
				// this.fmax = 500000.;
				// this.fmaxb = 2.5; /* newtons */
				this.pconv = 101.3; /* kilo-pascals */
				this.graphconv = 3.048; /* Conversion factor for graph paper (decimeters) */
			}

			/**
			 * English units.
			 */
			private void englishUnits() {
				this.lconv = 1.; /* feet */
				this.vconv = .6818;
				this.vmax = 250.; /* mph */
				if (this.planet == 2) {
					this.vmax = 50.;
				}
				this.fconv = 1.0;
				// this.fmax = 100000.;
				// this.fmaxb = .5; /* pounds */
				this.pconv = 14.7; /* lb/sq in */
				this.graphconv = 1.; /* Conversion factor for graph paper (feet) */
			}
		} // end of Dsys

		/** The Class Titl. */
		class MainMenu extends Panel {

			/** The Constant DESIGN_BTN_LBL. */
			private static final String DESIGN_BTN_LBL = "Design Tunnel Model";

			/** The Constant PROCESS_BTN_LBL. */
			private static final String PROCESS_BTN_LBL = "Process Tunnel Data";

			/** The Constant RESET_BTN_LBL. */
			private static final String RESET_BTN_LBL = Sys.RESET;

			/** The Constant serialVersionUID. */
			private static final long serialVersionUID = 1L;

			/** The Constant WIND_BTN_LBL. */
			private static final String WIND_BTN_LBL = "Wind Tunnel Test";

			/** The brst. */
			private Button designBtn;

			/** The bwtst. */
			private Button testBtn;

			/** The bpro. */
			private Button processBtn;

			/** The brst. */
			private Button resetBtn;

			/** The f 3. */
			TextField f1;

			/** The f 2. */
			private TextField f2;

			/** The f 3. */
			private TextField f3;

			/** The outerparent. */
			Sys outerparent;

			/**
			 * Instantiates a new titl.
			 *
			 * @param target
			 *            the target
			 */
			MainMenu(Sys target) {
				Sys.logger.info("Create MainMenu");
				this.setName("MainMenu");
				this.outerparent = target;
				this.setLayout(new GridLayout(13, 4, 5, 5));

				this.designBtn = new Button(MainMenu.DESIGN_BTN_LBL);
				this.designBtn.setBackground(Color.blue);
				this.designBtn.setForeground(Color.white);
				this.designBtn.addActionListener(new ActionListener(){  
				    public void actionPerformed(ActionEvent e){  
				    	MainMenu.this.showDesignCard();
			        }  
			    }); 

				this.f1 = new TextField("Develop Geometry", 5);
				this.f1.setBackground(Color.white);
				this.f1.setForeground(Color.blue);

				this.testBtn = new Button(MainMenu.WIND_BTN_LBL);
				this.testBtn.setBackground(Color.blue);
				this.testBtn.setForeground(Color.white);
				this.testBtn.addActionListener(new ActionListener(){  
				    public void actionPerformed(ActionEvent e){  
				    	MainMenu.this.showWindTunnelCard();
			        }  
			    }); 

				this.f2 = new TextField("Perform Test", 5);
				this.f2.setBackground(Color.white);
				this.f2.setForeground(Color.blue);

				this.processBtn = new Button(MainMenu.PROCESS_BTN_LBL);
				this.processBtn.setBackground(Color.blue);
				this.processBtn.setForeground(Color.white);
				this.processBtn.addActionListener(new ActionListener(){  
				    public void actionPerformed(ActionEvent e){  
				    	MainMenu.this.showProcessCard();
			        }  
			    }); 

				this.f3 = new TextField("Graph Results", 5);
				this.f3.setBackground(Color.white);
				this.f3.setForeground(Color.blue);

				this.resetBtn = new Button(MainMenu.RESET_BTN_LBL);
				this.resetBtn.setBackground(Color.magenta);
				this.resetBtn.setForeground(Color.white);
				this.resetBtn.addActionListener(new ActionListener(){  
				    public void actionPerformed(ActionEvent e){  
				    	MainMenu.this.restMenu();
			        }  
			    }); 				

				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));

				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label("TunnelSys Applet -  ", Label.RIGHT));
				this.add(new Label(" Version 1.0d ", Label.LEFT));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));

				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label("Process Buttons", Label.CENTER));
				this.add(new Label("Status", Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));

				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(this.designBtn);
				this.add(this.f1);
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));

				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(this.testBtn);
				this.add(this.f2);
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));

				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(this.processBtn);
				this.add(this.f3);
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));

				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(this.resetBtn);
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));

				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));

				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));

				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));

				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));

				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));

				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
				this.add(new Label(Sys.SPACE_STR, Label.CENTER));
			}


			/**
			 * Rest menu.
			 */
			private void restMenu() {
				this.f1.setText("Develop Geometry");
				this.f2.setText("Perform Test");
				this.f3.setText("Graph Results");
				this.f1.setForeground(Color.blue);
				this.f2.setForeground(Color.blue);
				this.f3.setForeground(Color.blue);
			}

			/**
			 * Show first card.
			 */
			public void showFirstCard() {
				Sys.logger.info("showFirstCard");
				Sys.this.getMainPanelLayout().show(Sys.this.mainPannel, MainPanel.FIRST_CARD);
				Sys.this.mainPannel.statusPanels();
			}

			/**
			 * Show process card.
			 */
			public void showProcessCard() {
				Sys.logger.info("showForthCard");
				Sys.this.getMainPanelLayout().show(Sys.this.mainPannel, MainPanel.FOURTH_CARD);
				Sys.this.mainPannel.statusPanels();
			}

			/**
			 * Show design card.
			 */
			public void showDesignCard() {
				Sys.logger.info("showSecondCard");
				Sys.this.getMainPanelLayout().show(Sys.this.mainPannel, MainPanel.SECOND_CARD);
				Sys.this.mainPannel.statusPanels();
			}

			/**
			 * Show wind tunnel card.
			 */
			public void showWindTunnelCard() {
				Sys.logger.info("showThirdCard");
				final CardLayout cardLayout = (CardLayout) Sys.this.mainPannel.getLayout();
				cardLayout.show(Sys.this.mainPannel, MainPanel.THIRD_CARD);
				Sys.this.mainPannel.statusPanels();
				// Sys.this.getMainPanelLayout().show(Sys.this.mainPannel,
				// MainPanel.THIRD_CARD);
			}
		} // end of Titl

		/** The Class Psys. */
		class ProcessCard extends Panel {

			/** The Class Cop. */
			class Cop extends Panel {

				/** The Constant GRAPH_CHOICE. */
				private static final String GRAPH_CHOICE = "Graph";

				/** The Constant MOVIE_CHOICE. */
				private static final String MOVIE_CHOICE = "Movie";

				/** The Constant serialVersionUID. */
				private static final long serialVersionUID = 1L;

				private TextField chordFld;

				/** The camber fld. */
				private TextField camberFld;

				/** The span fld. */
				private TextField spanFld;

				/** The thick fld. */
				private TextField thickFld;

				/** The aspect fld. */
				private TextField aspectFld;

				/** The wing area fld. */
				private TextField wingAreaFld;

				private TextField dynPressFld;

				/** The total press fld. */
				private TextField totalPressFld;

				/** The desity fld. */
				private TextField desityFld;

				/** The lift fld. */
				private TextField liftFld;

				private Label chordLbl;

				/** The camber lbl. */
				private Label camberLbl;

				/** The span lbl. */
				private Label spanLbl;

				/** The thick lbl. */
				private Label thickLbl;

				/** The aspect lbl. */
				private Label aspectLbl;

				/** The wing area lbl. */
				private Label wingAreaLbl;

				/** The dyn press lbl. */
				private Label dynPressLbl;

				/** The total press lbl. */
				private Label totalPressLbl;

				/** The density lbl. */
				private Label densityLbl;

				/** The lift lbl. */
				private Label liftLbl;

				/** The disch. */
				private Choice disch;

				/** The bdec. */
				private Button incBtn;

				/** The dec btn. */
				private Button decBtn;

				/** The pnt. */
				TextField mod;

				/** The type fld. */
				private TextField typeFld;

				/** The pnt. */
				private TextField pnt;

				/** The lab 2. */
				private Label selDataPntLbl;

				/** The disp lbl. */
				private Label dispLbl;

				/** The gem lbl. */
				private Label gemLbl;

				/** The diag lbl. */
				private Label diagLbl;

				private TextField speedFld;

				/** The ang of attack fld. */
				private TextField angOfAttackFld;

				/** The cl coeff fld. */
				private TextField clCoeffFld;

				/** The static press fld. */
				private TextField staticPressFld;

				private Label speedLbl;

				/** The ang of attack lbl. */
				private Label angOfAttackLbl;

				/** The cl coeff lbl. */
				private Label clCoeffLbl;

				/** The static press lbl. */
				private Label staticPressLbl;


				/**
				 * Instantiates a new cop.
				 *
				 * @param target
				 *            the target
				 */
				Cop(Sys target) {
					// this.outerparent = target;
					this.setLayout(new GridLayout(10, 4, 5, 5));

					this.gemLbl = new Label("Geometry:", Label.RIGHT);
					this.gemLbl.setForeground(Color.red);

					this.diagLbl = new Label("Diagnostics:", Label.RIGHT);
					this.diagLbl.setForeground(Color.red);

					this.selDataPntLbl = new Label("Select Data Point ", Label.LEFT);
					this.selDataPntLbl.setForeground(Color.blue);

					this.dispLbl = new Label("Display", Label.RIGHT);
					this.dispLbl.setForeground(Color.blue);

					this.disch = new Choice();
					this.disch.addItem(Cop.GRAPH_CHOICE);
					this.disch.addItem(Cop.MOVIE_CHOICE);
					this.disch.setBackground(Color.white);
					this.disch.setForeground(Color.blue);
					this.disch.select(0);

					this.pnt = new TextField("1", 5);
					this.pnt.setBackground(Color.white);
					this.pnt.setForeground(Color.black);

					this.incBtn = new Button(" + Increase");
					this.incBtn.setBackground(Color.blue);
					this.incBtn.setForeground(Color.white);

					this.decBtn = new Button(" - Decrease");
					this.decBtn.setBackground(Color.blue);
					this.decBtn.setForeground(Color.white);

					this.mod = new TextField("1", 5);
					this.mod.setBackground(Color.black);
					this.mod.setForeground(Color.yellow);

					this.typeFld = new TextField("1", 5);
					this.typeFld.setBackground(Color.black);
					this.typeFld.setForeground(Color.yellow);

					this.chordLbl = new Label(Sys.CHORD2, Label.CENTER);
					this.chordFld = new TextField(String.valueOf(Sys.filter3(ProcessCard.this.chord)), 5);
					this.chordFld.setBackground(Color.black);
					this.chordFld.setForeground(Color.yellow);

					this.camberLbl = new Label(Sys.CAMBER, Label.CENTER);
					this.camberFld = new TextField(Sys.ZERO_ZERO, 5);
					this.camberFld.setBackground(Color.black);
					this.camberFld.setForeground(Color.yellow);

					this.spanLbl = new Label("Span ", Label.CENTER);
					this.spanFld = new TextField(String.valueOf(Sys.filter3(ProcessCard.this.span)), 5);
					this.spanFld.setBackground(Color.black);
					this.spanFld.setForeground(Color.yellow);

					this.thickLbl = new Label(Sys.THICKNESS_SPACE, Label.CENTER);
					this.thickFld = new TextField(Sys.TWELVE_FIVE, 5);
					this.thickFld.setBackground(Color.black);
					this.thickFld.setForeground(Color.yellow);

					this.aspectLbl = new Label("Aspect Ratio ", Label.CENTER);
					this.aspectFld = new TextField(String.valueOf(Sys.filter3(ProcessCard.this.aspr)), 5);
					this.aspectFld.setBackground(Color.black);
					this.aspectFld.setForeground(Color.yellow);

					this.wingAreaLbl = new Label(Sys.WING_AREA, Label.CENTER);
					this.wingAreaFld = new TextField(String.valueOf(Sys.filter3(ProcessCard.this.area)), 5);
					this.wingAreaFld.setBackground(Color.black);
					this.wingAreaFld.setForeground(Color.yellow);

					this.dynPressLbl = new Label("Dynamic Press", Label.CENTER);
					this.dynPressFld = new TextField(Sys.TWELVE_FIVE, 5);
					this.dynPressFld.setBackground(Color.black);
					this.dynPressFld.setForeground(Color.yellow);

					this.totalPressLbl = new Label("Total Pressure ", Label.CENTER);
					this.totalPressFld = new TextField("1.5", 5);
					this.totalPressFld.setBackground(Color.black);
					this.totalPressFld.setForeground(Color.yellow);

					this.densityLbl = new Label(Sys.DENSITY, Label.CENTER);
					this.desityFld = new TextField(".00237", 5);
					this.desityFld.setBackground(Color.black);
					this.desityFld.setForeground(Color.yellow);

					this.liftLbl = new Label("Lift (L) ", Label.CENTER);
					this.liftFld = new TextField(Sys.ZERO_ZERO, 5);
					this.liftFld.setBackground(Color.black);
					this.liftFld.setForeground(Color.green);

					this.speedLbl = new Label("Speed ", Label.CENTER);
					this.speedFld = new TextField(Sys.ZERO_ZERO, 5);
					this.speedFld.setBackground(Color.black);
					this.speedFld.setForeground(Color.yellow);

					this.angOfAttackLbl = new Label("Angle of Attack ", Label.CENTER);
					this.angOfAttackFld = new TextField(Sys.ZERO_ZERO, 5);
					this.angOfAttackFld.setBackground(Color.black);
					this.angOfAttackFld.setForeground(Color.yellow);

					this.clCoeffLbl = new Label("Coefficient (Cl)", Label.CENTER);
					this.clCoeffFld = new TextField(Sys.ZERO_ZERO, 5);
					this.clCoeffFld.setBackground(Color.black);
					this.clCoeffFld.setForeground(Color.green);

					this.staticPressLbl = new Label("Static Pressure ", Label.CENTER);
					this.staticPressFld = new TextField(Sys.ZERO_ZERO, 5);
					this.staticPressFld.setBackground(Color.black);
					this.staticPressFld.setForeground(Color.yellow);
					this.add(this.diagLbl);
					this.add(this.selDataPntLbl);
					this.add(this.dispLbl);
					this.add(this.disch);

					this.add(this.decBtn);
					this.add(this.pnt);
					this.add(this.incBtn);
					this.add(new Label("", Label.CENTER));

					this.add(this.speedLbl);
					this.add(this.speedFld);
					this.add(this.angOfAttackLbl);
					this.add(this.angOfAttackFld);

					this.add(this.staticPressLbl);
					this.add(this.staticPressFld);
					this.add(this.densityLbl);
					this.add(this.desityFld);

					this.add(this.totalPressLbl);
					this.add(this.totalPressFld);
					this.add(this.dynPressLbl);
					this.add(this.dynPressFld);

					this.add(this.liftLbl);
					this.add(this.liftFld);
					this.add(this.clCoeffLbl);
					this.add(this.clCoeffFld);

					this.add(this.gemLbl);
					this.add(new Label("Model #", Label.RIGHT));
					this.add(this.mod);
					this.add(this.typeFld);

					this.add(this.chordLbl);
					this.add(this.chordFld);
					this.add(this.camberLbl);
					this.add(this.camberFld);

					this.add(this.spanLbl);
					this.add(this.spanFld);
					this.add(this.thickLbl);
					this.add(this.thickFld);

					this.add(this.aspectLbl);
					this.add(this.aspectFld);
					this.add(this.wingAreaLbl);
					this.add(this.wingAreaFld);
				}

				/**
				 * (non-Javadoc).
				 *
				 * @param evt
				 *            the evt
				 * @param arg
				 *            the arg
				 * @return true, if successful
				 * @see java.awt.Component#action(java.awt.Event, java.lang.Object)
				 */
				@Override
				public boolean action(Event evt, Object arg) {
					if (evt.target instanceof Choice) {
						this.handleCho(evt, arg);
						return true;
					}
					if (evt.target instanceof TextField) {
						this.handleBut(evt, arg);
						return true;
					}
					if (evt.target instanceof Button) {
						this.handleBut(evt, arg);
						return true;
					} else {
						return false;
					}
				} // handler

				/**
				 * Handle but.
				 *
				 * @param evt
				 *            the evt
				 * @param arg
				 *            the arg
				 */
				public void handleBut(Event evt, Object arg) {
					final String label = (String) arg;
					String outpres, outvel;
					Double V1;
					double v1;
					int i1;

					outpres = Sys.LBS_FT_2;
					if (ProcessCard.this.lunits == UNITS_METERIC) {
						outpres = Sys.K_PA;
					}
					outvel = " mph";
					if (ProcessCard.this.lunits == UNITS_METERIC) {
						outvel = " km/hr";
					}

					V1 = Double.valueOf(this.pnt.getText());
					v1 = V1.doubleValue();
					i1 = (int) v1;

					if (label.equals(" + Increase")) {
						i1 = i1 + 1;

						if (i1 >= ProcessCard.this.numpt[ProcessCard.this.tstflag]) {
							i1 = ProcessCard.this.numpt[ProcessCard.this.tstflag];
						}
						this.pnt.setText(String.valueOf(i1));
					}

					if (label.equals(" - Decrease")) {
						i1 = i1 - 1;

						if (i1 <= 1) {
							i1 = 1;
						}
						this.pnt.setText(String.valueOf(i1));
					}
					if (ProcessCard.this.tstflag >= 1) {

						ProcessCard.this.vfsd = ProcessCard.this.spd[ProcessCard.this.tstflag][i1]
								* ProcessCard.this.vcon2;
						ProcessCard.this.alfval = ProcessCard.this.ang[ProcessCard.this.tstflag][i1];
						ProcessCard.this.psin = ProcessCard.this.pin[ProcessCard.this.tstflag][i1]
								* ProcessCard.this.piconv;
						this.speedFld.setText(String
								.valueOf(Sys.filter3(
										ProcessCard.this.vcon2 * ProcessCard.this.spd[ProcessCard.this.tstflag][i1]))
								+ outvel);
						this.angOfAttackFld.setText(
								String.valueOf(Sys.filter3(ProcessCard.this.ang[ProcessCard.this.tstflag][i1])));
						this.staticPressFld.setText(String
								.valueOf(Sys.filter3(
										ProcessCard.this.piconv * ProcessCard.this.pin[ProcessCard.this.tstflag][i1]))
								+ outpres);

						ProcessCard.this.loadInput();
					}
				} // end handler

				/**
				 * Handle cho.
				 *
				 * @param evt
				 *            the evt
				 * @param arg
				 *            the arg
				 */
				public void handleCho(Event evt, Object arg) {
					final String label = (String) arg;
					if (label.equals(Cop.GRAPH_CHOICE)) {
						ProcessCard.this.layout.show(ProcessCard.this.processOutput, MainPanel.FIRST_CARD);
					}
					if (label.equals(Cop.MOVIE_CHOICE)) {
						ProcessCard.this.layout.show(ProcessCard.this.processOutput, MainPanel.SECOND_CARD);
					}
					ProcessCard.this.loadInput();
				} // handle choice
			} // Cop

			/** The Class Inp. */
			class ProcessInput extends Panel {

				/** The Class Cyl. */
				class Cyl extends Panel {

					/** The Class Inl. */
					class Inl extends Panel {

						/** The Constant serialVersionUID. */
						private static final long serialVersionUID = 1L;

						/** The f 3. */
						private TextField f1;

						/** The f 2. */
						private TextField f2;

						/** The f 3. */
						private TextField f3;

						/** The l 02. */
						private Label l01;

						/** The l 02. */
						private Label l02;

						/** The l 3. */
						private Label l1;

						/** The l 2. */
						private Label l2;

						/** The l 3. */
						private Label l3;

						/**
						 * The outerparent.
						 *
						 * @param target
						 *            the target
						 */
						// private Sys outerparent;

						/**
						 * Instantiates a new inl.
						 *
						 * @param target
						 *            the target
						 */
						Inl(Sys target) {

							// this.outerparent = target;
							this.setLayout(new GridLayout(5, 2, 2, 10));

							this.l01 = new Label("Cylinder-", Label.RIGHT);
							this.l01.setForeground(Color.blue);
							this.l02 = new Label("Ball Input", Label.LEFT);
							this.l02.setForeground(Color.blue);

							this.l1 = new Label("Spin rpm", Label.CENTER);
							this.f1 = new TextField(Sys.ZERO_ZERO, 5);

							this.l2 = new Label(Sys.RADIUS_FT, Label.CENTER);
							this.f2 = new TextField(".5", 5);

							this.l3 = new Label("Span ft", Label.CENTER);
							this.f3 = new TextField("5.0", 5);

							this.add(this.l01);
							this.add(this.l02);

							this.add(this.l1);
							this.add(this.f1);

							this.add(this.l2);
							this.add(this.f2);

							this.add(this.l3);
							this.add(this.f3);

							this.add(new Label(Sys.SPACE_STR, Label.CENTER));
							this.add(new Label(Sys.SPACE_STR, Label.CENTER));
						}

						/**
						 * (non-Javadoc).
						 *
						 * @param evt
						 *            the evt
						 * @return true, if successful
						 * @see java.awt.Component#handleEvent(java.awt.Event)
						 */
						@Override
						public boolean handleEvent(Event evt) {
							Double V1, V2, V3;
							double v1, v2, v3;
							float fl1;
							int i1, i2;
							if (evt.id == Event.ACTION_EVENT) {
								V1 = Double.valueOf(this.f1.getText());
								v1 = V1.doubleValue();
								V2 = Double.valueOf(this.f2.getText());
								v2 = V2.doubleValue();
								V3 = Double.valueOf(this.f3.getText());
								v3 = V3.doubleValue();

								ProcessCard.this.spin = v1;
								if (v1 < ProcessCard.this.spinmn) {
									ProcessCard.this.spin = v1 = ProcessCard.this.spinmn;
									fl1 = (float) v1;
									this.f1.setText(String.valueOf(fl1));
								}
								if (v1 > ProcessCard.this.spinmx) {
									ProcessCard.this.spin = v1 = ProcessCard.this.spinmx;
									fl1 = (float) v1;
									this.f1.setText(String.valueOf(fl1));
								}
								ProcessCard.this.spin = ProcessCard.this.spin / 60.0;

								ProcessCard.this.radius = v2;
								if (v2 < ProcessCard.this.radmn) {
									ProcessCard.this.radius = v2 = ProcessCard.this.radmn;
									fl1 = (float) v2;
									this.f2.setText(String.valueOf(fl1));
								}
								if (v2 > ProcessCard.this.radmx) {
									ProcessCard.this.radius = v2 = ProcessCard.this.radmx;
									fl1 = (float) v2;
									this.f2.setText(String.valueOf(fl1));
								}
								ProcessInput.this.cyl.setLims();

								v3 = ProcessCard.this.span;
								if (ProcessCard.this.foiltype == FOILTYPE_BALL) {
									ProcessCard.this.radius = ProcessCard.this.span;
									fl1 = (float) v3;
									this.f3.setText(String.valueOf(fl1));
								}
								ProcessCard.this.spanfac = (int) (ProcessCard.this.fact * ProcessCard.this.span
										/ ProcessCard.this.radius * .3535);
								ProcessCard.this.area = 2.0 * ProcessCard.this.radius * ProcessCard.this.span;
								if (ProcessCard.this.foiltype == FOILTYPE_BALL) {
									ProcessCard.this.area = Sys.PI * ProcessCard.this.radius * ProcessCard.this.radius;
								}

								i1 = (int) ((v1 - ProcessCard.this.spinmn)
										/ (ProcessCard.this.spinmx - ProcessCard.this.spinmn) * 1000.);
								i2 = (int) ((v2 - ProcessCard.this.radmn)
										/ (ProcessCard.this.radmx - ProcessCard.this.radmn) * 1000.);

								Sys.MainPanel.ProcessCard.ProcessInput.Cyl.this.inr.s1.setValue(i1);
								Sys.MainPanel.ProcessCard.ProcessInput.Cyl.this.inr.s2.setValue(i2);

								ProcessCard.this.computeFlow();
								return true;
							} else {
								return false;
							}
						} // Handler
					} // Inl

					/** The Class Inr. */
					class Inr extends Panel {

						/** The Constant BALL. */
						private static final String BALL = "Ball";

						/** The Constant CYLINDER. */
						public static final String CYLINDER = "Cylinder";

						/** The Constant serialVersionUID. */
						private static final long serialVersionUID = 1L;

						/** The outerparent. */
						// private Sys outerparent;

						/** The s 2. */
						private Scrollbar s1;

						/** The s 2. */
						private Scrollbar s2;

						/** The shapch. */
						private Choice shapch;

						/**
						 * Instantiates a new inr.
						 *
						 * @param target
						 *            the target
						 */
						Inr(Sys target) {
							int i1, i2;
							// this.outerparent = target;
							this.setLayout(new GridLayout(5, 1, 2, 10));

							i1 = (int) ((ProcessCard.this.spin * 60.0 - ProcessCard.this.spinmn)
									/ (ProcessCard.this.spinmx - ProcessCard.this.spinmn) * 1000.);
							i2 = (int) ((ProcessCard.this.radius - ProcessCard.this.radmn)
									/ (ProcessCard.this.radmx - ProcessCard.this.radmn) * 1000.);

							this.s1 = new Scrollbar(Scrollbar.HORIZONTAL, i1, 10, 0, 1000);
							this.s2 = new Scrollbar(Scrollbar.HORIZONTAL, i2, 10, 0, 1000);

							this.shapch = new Choice();
							this.shapch.addItem(Sys.AIRFOIL);
							this.shapch.addItem(Sys.ELLIPSE);
							this.shapch.addItem(Sys.PLATE);
							this.shapch.addItem(Inr.CYLINDER);
							this.shapch.addItem(Inr.BALL);
							this.shapch.setBackground(Color.white);
							this.shapch.setForeground(Color.blue);
							this.shapch.select(0);

							this.add(this.shapch);
							this.add(this.s1);
							this.add(this.s2);
							this.add(new Label(Sys.SPACE_STR, Label.CENTER));
						}

						/**
						 * Handle bar.
						 *
						 * @param evt
						 *            the evt
						 */
						public void handleBar(Event evt) {
							int i1, i2;
							double v1, v2, v3;
							float fl1, fl2, fl3;

							// Input for computations
							i1 = this.s1.getValue();
							i2 = this.s2.getValue();

							ProcessCard.this.spin = v1 = i1 * (ProcessCard.this.spinmx - ProcessCard.this.spinmn)
									/ 1000. + ProcessCard.this.spinmn;
							ProcessCard.this.spin = ProcessCard.this.spin / 60.0;
							ProcessCard.this.radius = v2 = i2 * (ProcessCard.this.radmx - ProcessCard.this.radmn)
									/ 1000. + ProcessCard.this.radmn;
							v3 = ProcessCard.this.span;
							if (ProcessCard.this.foiltype == FOILTYPE_BALL) {
								ProcessCard.this.radius = v3;
							}
							ProcessCard.this.spanfac = (int) (ProcessCard.this.fact * ProcessCard.this.span
									/ ProcessCard.this.radius * .3535);
							ProcessCard.this.area = 2.0 * ProcessCard.this.radius * ProcessCard.this.span;
							if (ProcessCard.this.foiltype == FOILTYPE_BALL) {
								ProcessCard.this.area = Sys.PI * ProcessCard.this.radius * ProcessCard.this.radius;
							}
							ProcessInput.this.cyl.setLims();

							fl1 = (float) v1;
							fl2 = (float) v2;
							fl3 = (float) v3;

							Sys.MainPanel.ProcessCard.ProcessInput.Cyl.this.inl.f1.setText(String.valueOf(fl1));
							Sys.MainPanel.ProcessCard.ProcessInput.Cyl.this.inl.f2.setText(String.valueOf(fl2));
							Sys.MainPanel.ProcessCard.ProcessInput.Cyl.this.inl.f3.setText(String.valueOf(fl3));

							ProcessCard.this.computeFlow();
						}

						/**
						 * Handle cho.
						 *
						 * @param evt
						 *            the evt
						 */
						public void handleCho(Event evt) {
							ProcessCard.this.foiltype = this.shapch.getSelectedIndex() + 1;
							if (ProcessCard.this.foiltype >= 4) {
								ProcessCard.this.alfval = 0.0;
							}
							if (ProcessCard.this.foiltype <= FOILTYPE_ELLIPTICAL) {
								ProcessCard.this.layin.show(ProcessCard.this.processInput, MainPanel.SECOND_CARD);
							}
							if (ProcessCard.this.foiltype == FOILTYPE_PLATE) {
								ProcessCard.this.layin.show(ProcessCard.this.processInput, MainPanel.SECOND_CARD);
								ProcessCard.this.thkinpt = ProcessCard.this.thkmn;
								ProcessCard.this.thkval = ProcessCard.this.thkinpt / 25.0;
							}
							if (ProcessCard.this.foiltype == FOILTYPE_CYLINDER) {
								ProcessCard.this.layin.show(ProcessCard.this.processInput, MainPanel.FOURTH_CARD);
							}
							if (ProcessCard.this.foiltype == FOILTYPE_BALL) {
								ProcessCard.this.radius = ProcessCard.this.span;
								ProcessCard.this.area = Sys.PI * ProcessCard.this.radius * ProcessCard.this.radius;
								ProcessCard.this.layin.show(ProcessCard.this.processInput, MainPanel.FOURTH_CARD);
								if (ProcessCard.this.viewflg != WALL_VIEW_TRANSPARENT) {
									ProcessCard.this.viewflg = WALL_VIEW_TRANSPARENT;
								}
							}

							ProcessCard.this.outopt = 0;
							ProcessCard.this.dispp = 0;
							ProcessCard.this.calcrange = 0;

							ProcessCard.this.loadInput();
						} // handler

						/**
						 * (non-Javadoc).
						 *
						 * @param evt
						 *            the evt
						 * @return true, if successful
						 * @see java.awt.Component#handleEvent(java.awt.Event)
						 */
						@Override
						public boolean handleEvent(Event evt) {
							if (evt.id == Event.ACTION_EVENT) {
								this.handleCho(evt);
								return true;
							}
							if (evt.id == Event.SCROLL_ABSOLUTE) {
								this.handleBar(evt);
								return true;
							}
							if (evt.id == Event.SCROLL_LINE_DOWN) {
								this.handleBar(evt);
								return true;
							}
							if (evt.id == Event.SCROLL_LINE_UP) {
								this.handleBar(evt);
								return true;
							}
							if (evt.id == Event.SCROLL_PAGE_DOWN) {
								this.handleBar(evt);
								return true;
							}
							if (evt.id == Event.SCROLL_PAGE_UP) {
								this.handleBar(evt);
								return true;
							} else {
								return false;
							}
						}
					} // Inr

					/** The Constant serialVersionUID. */
					private static final long serialVersionUID = 1L;

					/** The inl. */
					Inl inl;

					/** The inr. */
					Inr inr;

					/** The outerparent. */
					Sys outerparent;

					/**
					 * Instantiates a new cyl.
					 *
					 * @param target
					 *            the target
					 */
					Cyl(Sys target) {

						this.outerparent = target;
						this.setLayout(new GridLayout(1, 2, 5, 5));

						this.inl = new Inl(this.outerparent);
						this.inr = new Inr(this.outerparent);

						this.add(this.inl);
						this.add(this.inr);
					}

					/** Sets the lims. */
					public void setLims() {
						float fl1;
						int i1;

						ProcessCard.this.spinmx = 2.75 * ProcessCard.this.vfsd / ProcessCard.this.vconv
								/ (ProcessCard.this.radius / ProcessCard.this.lconv);
						ProcessCard.this.spinmn = -2.75 * ProcessCard.this.vfsd / ProcessCard.this.vconv
								/ (ProcessCard.this.radius / ProcessCard.this.lconv);
						if (ProcessCard.this.spin * 60.0 < ProcessCard.this.spinmn) {
							ProcessCard.this.spin = ProcessCard.this.spinmn / 60.0;
							fl1 = (float) (ProcessCard.this.spin * 60.0);
							this.inl.f1.setText(String.valueOf(fl1));
						}
						if (ProcessCard.this.spin * 60.0 > ProcessCard.this.spinmx) {
							ProcessCard.this.spin = ProcessCard.this.spinmx / 60.0;
							fl1 = (float) (ProcessCard.this.spin * 60.0);
							this.inl.f1.setText(String.valueOf(fl1));
						}
						i1 = (int) ((60 * ProcessCard.this.spin - ProcessCard.this.spinmn)
								/ (ProcessCard.this.spinmx - ProcessCard.this.spinmn) * 1000.);
						this.inr.s1.setValue(i1);
					}
				} // Cyl

				/** The Class Flt. */
				class Flt extends Panel {

					/** The Class Lwr. */
					class Lwr extends Panel {

						/** The Constant CL_VS_ANGLE. */
						public static final String CL_VS_ANGLE = "Cl vs Angle";

						/** The Constant CL_VS_PRESSURE. */
						public static final String CL_VS_PRESSURE = "Cl vs Pressure";

						/** The Constant CL_VS_SPEED. */
						public static final String CL_VS_SPEED = "Cl vs Speed";

						/** The Constant IMPERIAL_UNITS. */
						public static final String IMPERIAL_UNITS = "Imperial Units";

						/** The Constant L_VS_ANGLE. */
						public static final String L_VS_ANGLE = "L vs Angle";

						/** The Constant L_VS_PRESSURE. */
						public static final String L_VS_PRESSURE = "L vs Pressure";

						/** The Constant L_VS_SPEED. */
						public static final String L_VS_SPEED = "L vs Speed";

						/** The Constant METRIC_UNITS. */
						public static final String METRIC_UNITS = "Metric Units";

						/** The Constant PLOT_SELECTION. */
						public static final String PLOT_SELECTION = "Plot Selection";

						/** The Constant serialVersionUID. */
						public static final long serialVersionUID = 1L;

						/** The endit. */
						private Button restBtn;

						/** The endit. */
						private Button returBtn;

						/** The d 4. */
						// private TextField d1;
						// private TextField d2;
						// private TextField d3;
						// private TextField d4;

						/** The l 1. */
						private Label l1;

						/** The outerparent. */
						// private Sys outerparent;

						/** The pl 8. */
						// private Button pl1;
						private Button pl2;

						/** The pl 3. */
						private Button pl3;

						/** The pl 4. */
						private Button pl4;

						/** The pl 5. */
						private Button pl5;

						/** The pl 6. */
						private Button pl6;

						/** The pl 7. */
						private Button pl7;
						// private Button pl8;

						/** The plout. */
						private Choice untch;
						// private Choice plout;

						/**
						 * Instantiates a new lwr.
						 *
						 * @param target
						 *            the target
						 */
						Lwr(Sys target) {

							// this.outerparent = target;
							this.setLayout(new GridLayout(5, 3, 2, 5));

							this.l1 = new Label(Lwr.PLOT_SELECTION, Label.CENTER);
							this.l1.setForeground(Color.red);

							this.restBtn = new Button(Sys.RESET);
							this.restBtn.setBackground(Color.magenta);
							this.restBtn.setForeground(Color.white);

							this.returBtn = new Button(Sys.RETURN);
							this.returBtn.setBackground(Color.red);
							this.returBtn.setForeground(Color.white);

							this.untch = new Choice();
							this.untch.addItem(Lwr.IMPERIAL_UNITS);
							this.untch.addItem(Lwr.METRIC_UNITS);
							this.untch.setBackground(Color.white);
							this.untch.setForeground(Color.red);
							this.untch.select(0);

							this.pl2 = new Button(Lwr.CL_VS_ANGLE);
							this.pl2.setBackground(Color.blue);
							this.pl2.setForeground(Color.white);

							this.pl3 = new Button(Lwr.L_VS_ANGLE);
							this.pl3.setBackground(Color.blue);
							this.pl3.setForeground(Color.white);

							this.pl4 = new Button(Lwr.L_VS_SPEED);
							this.pl4.setBackground(Color.blue);
							this.pl4.setForeground(Color.white);

							this.pl5 = new Button(Lwr.L_VS_PRESSURE);
							this.pl5.setBackground(Color.blue);
							this.pl5.setForeground(Color.white);

							this.pl6 = new Button(Lwr.CL_VS_SPEED);
							this.pl6.setBackground(Color.blue);
							this.pl6.setForeground(Color.white);

							this.pl7 = new Button(Lwr.CL_VS_PRESSURE);
							this.pl7.setBackground(Color.blue);
							this.pl7.setForeground(Color.white);

							// this.d1 = new TextField(Sys.ZERO_ZERO, 5);
							// this.d2 = new TextField(Sys.ZERO_ZERO, 5);
							// this.d3 = new TextField(Sys.ZERO_ZERO, 5);

							this.add(new Label("", Label.CENTER));
							this.add(this.l1);
							this.add(new Label("", Label.CENTER));
							this.add(this.pl3);
							this.add(this.pl5);
							this.add(this.pl4);

							this.add(this.pl2);
							this.add(this.pl7);
							this.add(this.pl6);
							/* add(d1); add(d2); add(d3); */
							this.add(new Label("", Label.CENTER));
							this.add(new Label("", Label.CENTER));
							this.add(new Label("", Label.CENTER));

							this.add(this.returBtn);
							this.add(this.restBtn);
							this.add(this.untch);
						}

						/**
						 * (non-Javadoc).
						 *
						 * @param evt
						 *            the evt
						 * @param arg
						 *            the arg
						 * @return true, if successful
						 * @see java.awt.Component#action(java.awt.Event, java.lang.Object)
						 */
						@Override
						public boolean action(Event evt, Object arg) {
							if (evt.target instanceof Choice) {
								this.handleCho(evt, arg);
								return true;
							}
							if (evt.target instanceof Button) {
								this.handleRefs(evt, arg);
								return true;
							} else {
								return false;
							}
						} // handler

						/**
						 * Color buttons.
						 *
						 * @param b
						 *            the b
						 * @param dispp
						 *            the dispp
						 * @param dout
						 *            the dout
						 */
						private void colorButtons(Button b, int dispp, int dout) {
							ProcessCard.this.dispp = dispp;
							ProcessCard.this.dout = dout;
							final List<Button> buttons = new ArrayList<Button>();
							buttons.add(this.pl2);
							buttons.add(this.pl3);
							buttons.add(this.pl4);
							buttons.add(this.pl5);
							buttons.add(this.pl6);
							buttons.add(this.pl7);
							for (final Button button : buttons) {
								if (button == b) {
									button.setBackground(Color.yellow);
									button.setForeground(Color.black);
								} else {
									button.setBackground(Color.blue);
									button.setForeground(Color.white);
								}
							}
							for (int i = 1; i <= 20; ++i) {
								ProcessCard.this.plsav[i] = 0;
							}
							ProcessCard.this.loadInput();
						}

						/**
						 * Handle cho.
						 *
						 * @param evt
						 *            the evt
						 * @param arg
						 *            the arg
						 */
						public void handleCho(Event evt, Object arg) {
							ProcessCard.this.lunits = this.untch.getSelectedIndex();
							ProcessCard.this.setUnits();
							ProcessCard.this.loadInput();

							ProcessCard.this.computeFlow();
						} // handle choice

						/**
						 * Handle refs.
						 *
						 * @param evt
						 *            the evt
						 * @param arg
						 *            the arg
						 */
						public void handleRefs(Event evt, Object arg) {
							final String label = (String) arg;
							if (label.equals(Sys.RETURN)) {
								Sys.this.mainPannel.mainMenu.f3.setText("Completed");
								Sys.this.mainPannel.mainMenu.f3.setForeground(Color.red);
								Sys.this.mainPannel.mainMenu.showFirstCard();
							}
							if (label.equals(Sys.RESET)) {
								ProcessCard.this.processInput.flt.upr.inl.loadDataBtn.setBackground(Color.blue);
								ProcessCard.this.processInput.flt.upr.inl.loadDataBtn.setForeground(Color.white);
								ProcessCard.this.solvep.setDefaults();
								ProcessCard.this.layin.show(ProcessCard.this.processInput, MainPanel.FIRST_CARD);
								this.untch.select(ProcessCard.this.lunits);
								// **** the lunits check MUST come first
								ProcessCard.this.setUnits();
								ProcessCard.this.outopt = 0;

								ProcessCard.this.loadInput();
							}
							if (label.equals(Lwr.CL_VS_ANGLE)) {
								this.colorButtons(this.pl2, 2, 1);
							}
							if (label.equals(Lwr.L_VS_ANGLE)) {
								this.colorButtons(this.pl3, 2, 0);
							}
							if (label.equals(Lwr.L_VS_SPEED)) {
								this.colorButtons(this.pl4, 5, 0);
							}
							if (label.equals(Lwr.L_VS_PRESSURE)) {
								this.colorButtons(this.pl5, 9, 0);
							}
							if (label.equals(Lwr.CL_VS_SPEED)) {
								this.colorButtons(this.pl6, 5, 1);
							}
							if (label.equals(Lwr.CL_VS_PRESSURE)) {
								this.colorButtons(this.pl7, 9, 1);
							}
						}
					} // Lwr

					/** The Class Upr. */
					class Upr extends Panel {

						/** The Class Inl. */
						class Inl extends Panel {

							/** The Constant serialVersionUID. */
							private static final long serialVersionUID = 1L;

							/** The bprto. */
							private Button dataFileBtn;

							/** The bto. */
							private Button loadDataBtn;

							/** The bprt. */
							private Button printFileBtn;

							/** The bprto. */
							private Button openPrintBtn;

							/** The btload. */
							private Button openTestBtn;

							/** The l 5. */
							private Label l1;

							/** The l 2. */
							private Label l2;

							/** The mod. */
							private TextField mod;

							/**
							 * The outerparent.
							 *
							 * @param target
							 *            the target
							 */
							// private Sys outerparent;

							/**
							 * Instantiates a new inl.
							 *
							 * @param target
							 *            the target
							 */
							Inl(Sys target) {

								// this.outerparent = target;
								this.setLayout(new GridLayout(6, 2, 2, 5));

								this.dataFileBtn = new Button("Data File");
								this.dataFileBtn.setBackground(Color.blue);
								this.dataFileBtn.setForeground(Color.white);

								this.loadDataBtn = new Button("Load Data");
								this.loadDataBtn.setBackground(Color.blue);
								this.loadDataBtn.setForeground(Color.white);

								this.l1 = new Label("Select Test#:", Label.RIGHT);
								this.l1.setForeground(Color.blue);

								this.l2 = new Label("Type of Test:", Label.RIGHT);
								this.l2.setForeground(Color.blue);

								this.mod = new TextField("1", 5);
								this.mod.setBackground(Color.white);
								this.mod.setForeground(Color.blue);

								this.openTestBtn = new Button("Open Test");
								this.openTestBtn.setBackground(Color.blue);
								this.openTestBtn.setForeground(Color.white);

								this.printFileBtn = new Button("Print File");
								this.printFileBtn.setBackground(Color.blue);
								this.printFileBtn.setForeground(Color.white);

								this.openPrintBtn = new Button("Open Print");
								this.openPrintBtn.setBackground(Color.blue);
								this.openPrintBtn.setForeground(Color.white);

								this.add(new Label("", Label.CENTER));
								this.add(this.loadDataBtn);

								this.add(this.l1);
								this.add(this.mod);

								this.add(new Label("", Label.CENTER));
								this.add(this.openTestBtn);

								this.add(new Label("", Label.CENTER));
								this.add(this.l2);

								this.add(new Label("", Label.CENTER));
								this.add(new Label("", Label.CENTER));

								this.add(new Label("", Label.CENTER));
								this.add(new Label("", Label.CENTER));
							}

							/**
							 * (non-Javadoc).
							 *
							 * @param evt
							 *            the evt
							 * @param arg
							 *            the arg
							 * @return true, if successful
							 * @see java.awt.Component#action(java.awt.Event, java.lang.Object)
							 */
							@Override
							public boolean action(Event evt, Object arg) {
								if (evt.target instanceof Button) {
									this.handleBut(evt, arg);
									return true;
								} else {
									return false;
								}
							} // handler

							/**
							 * Handle but.
							 *
							 * @param evt
							 *            the evt
							 * @param arg
							 *            the arg
							 */
							public void handleBut(Event evt, Object arg) {
								final String label = (String) arg;
								String labmod, labrd, labnum, labv, laba, labp;
								String outvel, outpres;
								Double V1;
								double v1;
								final double buf[][] = new double[10][801];
								final int ibuf[][] = new int[7][801];
								int i1, i2, ikeep, icount;
								int i, j;
								labmod = "  tests";
								labrd = "File Read -";
								labnum = " data points for this test";
								labv = " Vary Speed";
								laba = " Vary Angle of Attack";
								labp = " Vary Pressure";
								ikeep = 1;
								if (label.equals("Load Data")) {
									for (i = 1; i <= 20; ++i) {
										ProcessCard.this.plsav[i] = 0;
									}
									ProcessCard.this.tstflag = 0;
									this.loadDataBtn.setBackground(Color.yellow);
									this.loadDataBtn.setForeground(Color.black);
									this.openTestBtn.setBackground(Color.blue);
									this.openTestBtn.setForeground(Color.white);
									// read in data
									for (i = 1; i <= Sys.this.datnum; ++i) {
										ikeep = i - 1;
										for (j = 1; j <= 9; ++j) {
											buf[j][i] = Sys.bsav[j][i];
										}
										for (j = 1; j <= 6; ++j) {
											ibuf[j][i] = Sys.bsavi[j][i];
										}
									}
									// find number of points in each test
									ProcessCard.this.numtest = 1;
									for (i = 1; i <= ikeep; ++i) {
										if (ibuf[1][i] > ProcessCard.this.numtest) {
											ProcessCard.this.numpt[ProcessCard.this.numtest] = ibuf[2][i - 1] + 1;
											ProcessCard.this.numtest = ProcessCard.this.numtest + 1;
										}
									}
									ProcessCard.this.numpt[ProcessCard.this.numtest] = ibuf[2][ikeep] + 1;
									icount = 0;
									for (i = 1; i <= ProcessCard.this.numtest; ++i) {
										for (j = 1; j <= ProcessCard.this.numpt[i]; ++j) {
											icount = icount + 1;
											ProcessCard.this.dcam[i] = buf[1][icount];
											ProcessCard.this.dthk[i] = buf[2][icount];
											ProcessCard.this.dchrd[i] = buf[3][icount];
											ProcessCard.this.dspan[i] = buf[4][icount];
											ProcessCard.this.qdt[i][j] = buf[5][icount];
											ProcessCard.this.dlft[i][j] = buf[6][icount];
											ProcessCard.this.dspd[i][j] = buf[7][icount];
											ProcessCard.this.ang[i][j] = buf[8][icount];
											ProcessCard.this.dpin[i][j] = buf[9][icount];
											ProcessCard.this.modlnm[i] = ibuf[3][icount];
											ProcessCard.this.dlunits[i] = ibuf[4][icount];
											ProcessCard.this.testp[i] = ibuf[5][icount];
											ProcessCard.this.dftp[i] = ibuf[6][icount];
										}
									}
									Sys.MainPanel.ProcessCard.ProcessInput.Flt.Upr.this.inr.modd
											.setText(labrd + ProcessCard.this.numtest + labmod);
								}

								if (label.equals("Open Test")) {
									this.openTestBtn.setBackground(Color.yellow);
									this.openTestBtn.setForeground(Color.blue);

									V1 = Double.valueOf(this.mod.getText());
									v1 = V1.doubleValue();
									i2 = (int) v1;

									ProcessCard.this.tstflag = i2;
									if (i2 >= ProcessCard.this.numtest) {
										i2 = ProcessCard.this.numtest;
										this.mod.setText(String.valueOf(i2));
									}

									i1 = ProcessCard.this.modlnm[i2];
									ProcessCard.this.cop.pnt.setText("1");
									ProcessCard.this.cop.mod.setText(String.valueOf(i1));
									ProcessCard.this.foiltype = ProcessCard.this.dftp[i2];
									ProcessCard.this.lunits = ProcessCard.this.dlunits[i2];
									ProcessCard.this.setUnits();
									if (ProcessCard.this.lunits == UNITS_ENGLISH) {
										ProcessCard.this.processInput.flt.lwr.untch.select(0);
									}
									if (ProcessCard.this.lunits == UNITS_METERIC) {
										ProcessCard.this.processInput.flt.lwr.untch.select(1);
									}
									outpres = Sys.LBS_FT_2;
									if (ProcessCard.this.lunits == UNITS_METERIC) {
										outpres = Sys.K_PA;
									}
									outvel = " mph";
									if (ProcessCard.this.lunits == UNITS_METERIC) {
										outvel = " km/hr";
									}

									ProcessCard.this.camval = ProcessCard.this.dcam[i2];
									ProcessCard.this.thkval = ProcessCard.this.dthk[i2];
									ProcessCard.this.span = ProcessCard.this.dspan[i2];
									ProcessCard.this.chord = ProcessCard.this.dchrd[i2];
									Sys.MainPanel.ProcessCard.ProcessInput.Flt.Upr.this.inr.modt
											.setText(String.valueOf(ProcessCard.this.numpt[i2]) + labnum);
									if (ProcessCard.this.testp[i2] == 1) {
										Sys.MainPanel.ProcessCard.ProcessInput.Flt.Upr.this.inr.ttest.setText(labv);
										ProcessCard.this.dispp = 5;
										ProcessCard.this.dout = 0;
										ProcessCard.this.processInput.flt.lwr.pl2.setBackground(Color.blue);
										ProcessCard.this.processInput.flt.lwr.pl2.setForeground(Color.white);
										ProcessCard.this.processInput.flt.lwr.pl3.setBackground(Color.blue);
										ProcessCard.this.processInput.flt.lwr.pl3.setForeground(Color.white);
										ProcessCard.this.processInput.flt.lwr.pl4.setBackground(Color.yellow);
										ProcessCard.this.processInput.flt.lwr.pl4.setForeground(Color.black);
										ProcessCard.this.processInput.flt.lwr.pl5.setBackground(Color.blue);
										ProcessCard.this.processInput.flt.lwr.pl5.setForeground(Color.white);
										ProcessCard.this.processInput.flt.lwr.pl6.setBackground(Color.blue);
										ProcessCard.this.processInput.flt.lwr.pl6.setForeground(Color.white);
										ProcessCard.this.processInput.flt.lwr.pl7.setBackground(Color.blue);
										ProcessCard.this.processInput.flt.lwr.pl7.setForeground(Color.white);
									}
									if (ProcessCard.this.testp[i2] == 2) {
										Sys.MainPanel.ProcessCard.ProcessInput.Flt.Upr.this.inr.ttest.setText(laba);
										ProcessCard.this.dispp = 2;
										ProcessCard.this.dout = 0;
										ProcessCard.this.processInput.flt.lwr.pl2.setBackground(Color.blue);
										ProcessCard.this.processInput.flt.lwr.pl2.setForeground(Color.white);
										ProcessCard.this.processInput.flt.lwr.pl3.setBackground(Color.yellow);
										ProcessCard.this.processInput.flt.lwr.pl3.setForeground(Color.black);
										ProcessCard.this.processInput.flt.lwr.pl4.setBackground(Color.blue);
										ProcessCard.this.processInput.flt.lwr.pl4.setForeground(Color.white);
										ProcessCard.this.processInput.flt.lwr.pl5.setBackground(Color.blue);
										ProcessCard.this.processInput.flt.lwr.pl5.setForeground(Color.white);
										ProcessCard.this.processInput.flt.lwr.pl6.setBackground(Color.blue);
										ProcessCard.this.processInput.flt.lwr.pl6.setForeground(Color.white);
										ProcessCard.this.processInput.flt.lwr.pl7.setBackground(Color.blue);
										ProcessCard.this.processInput.flt.lwr.pl7.setForeground(Color.white);
									}
									if (ProcessCard.this.testp[i2] == 3) {
										Sys.MainPanel.ProcessCard.ProcessInput.Flt.Upr.this.inr.ttest.setText(labp);
										ProcessCard.this.dispp = 9;
										ProcessCard.this.dout = 0;
										ProcessCard.this.processInput.flt.lwr.pl2.setBackground(Color.blue);
										ProcessCard.this.processInput.flt.lwr.pl2.setForeground(Color.white);
										ProcessCard.this.processInput.flt.lwr.pl3.setBackground(Color.blue);
										ProcessCard.this.processInput.flt.lwr.pl3.setForeground(Color.white);
										ProcessCard.this.processInput.flt.lwr.pl4.setBackground(Color.blue);
										ProcessCard.this.processInput.flt.lwr.pl4.setForeground(Color.white);
										ProcessCard.this.processInput.flt.lwr.pl5.setBackground(Color.yellow);
										ProcessCard.this.processInput.flt.lwr.pl5.setForeground(Color.black);
										ProcessCard.this.processInput.flt.lwr.pl6.setBackground(Color.blue);
										ProcessCard.this.processInput.flt.lwr.pl6.setForeground(Color.white);
										ProcessCard.this.processInput.flt.lwr.pl7.setBackground(Color.blue);
										ProcessCard.this.processInput.flt.lwr.pl7.setForeground(Color.white);
									}
									ProcessCard.this.chrdfac = Math
											.sqrt(ProcessCard.this.chord / ProcessCard.this.lconv);
									ProcessCard.this.fact = 32.0 * ProcessCard.this.chrdfac;
									// con.o2.setText(String.valueOf(chord));

									for (j = 1; j <= ProcessCard.this.numpt[ProcessCard.this.tstflag]; ++j) {
										ProcessCard.this.lft[ProcessCard.this.tstflag][j] = ProcessCard.this.dlft[ProcessCard.this.tstflag][j]
												/ ProcessCard.this.fconv;
										ProcessCard.this.spd[ProcessCard.this.tstflag][j] = ProcessCard.this.dspd[ProcessCard.this.tstflag][j]
												/ ProcessCard.this.vcon2;
										ProcessCard.this.pin[ProcessCard.this.tstflag][j] = ProcessCard.this.dpin[ProcessCard.this.tstflag][j]
												/ ProcessCard.this.piconv;
									}
									ProcessCard.this.vfsd = ProcessCard.this.vcon2
											* ProcessCard.this.spd[ProcessCard.this.tstflag][1];
									ProcessCard.this.alfval = ProcessCard.this.ang[ProcessCard.this.tstflag][1];
									ProcessCard.this.psin = ProcessCard.this.piconv
											* ProcessCard.this.pin[ProcessCard.this.tstflag][1];

									ProcessCard.this.cop.speedFld
											.setText(String
													.valueOf(Sys.filter3(ProcessCard.this.vcon2
															* ProcessCard.this.spd[ProcessCard.this.tstflag][1]))
													+ outvel);
									ProcessCard.this.cop.angOfAttackFld.setText(String
											.valueOf(Sys.filter3(ProcessCard.this.ang[ProcessCard.this.tstflag][1])));
									ProcessCard.this.cop.staticPressFld
											.setText(String
													.valueOf(Sys.filter3(ProcessCard.this.piconv
															* ProcessCard.this.pin[ProcessCard.this.tstflag][1]))
													+ outpres);

									ProcessCard.this.loadInput();
								}
							} // end handler
						} // Inl

						/** The Class Inr. */
						class Inr extends Panel {

							/** The Constant serialVersionUID. */
							private static final long serialVersionUID = 1L;

							/** The ttest. */
							private TextField modt;

							/** The modd. */
							private TextField modd;

							/** The modp. */
							private TextField modp;

							/** The ttest. */
							private TextField ttest;

							/**
							 * The outerparent.
							 *
							 * @param target
							 *            the target
							 */
							// private Sys outerparent;

							/**
							 * Instantiates a new inr.
							 *
							 * @param target
							 *            the target
							 */
							Inr(Sys target) {

								// this.outerparent = target;
								this.setLayout(new GridLayout(6, 1, 2, 5));

								this.modd = new TextField("<-Load Test  Data");
								this.modd.setBackground(Color.white);
								this.modd.setForeground(Color.blue);

								this.modt = new TextField("Number of Test Points ");
								this.modt.setBackground(Color.white);
								this.modt.setForeground(Color.blue);

								this.ttest = new TextField("Test Type ");
								this.ttest.setBackground(Color.white);
								this.ttest.setForeground(Color.blue);

								this.modp = new TextField("Print File ");
								this.modp.setBackground(Color.white);
								this.modp.setForeground(Color.blue);

								this.add(this.modd);
								this.add(new Label("", Label.CENTER));
								this.add(this.modt);
								this.add(this.ttest);
								this.add(new Label("", Label.CENTER));
								this.add(new Label("", Label.CENTER));
							}

						} // Inr

						/** The Constant serialVersionUID. */
						private static final long serialVersionUID = 1L;

						/** The inl. */
						Inl inl;

						/** The inr. */
						Inr inr;

						/** The outerparent. */
						Sys outerparent;

						/**
						 * Instantiates a new upr.
						 *
						 * @param target
						 *            the target
						 */
						Upr(Sys target) {

							this.outerparent = target;
							this.setLayout(new GridLayout(1, 2, 5, 5));

							this.inl = new Inl(this.outerparent);
							this.inr = new Inr(this.outerparent);

							this.add(this.inl);
							this.add(this.inr);
						}
					} // Upr

					/** The Constant serialVersionUID. */
					private static final long serialVersionUID = 1L;

					/** The lwr. */
					Lwr lwr;

					/** The outerparent. */
					Sys outerparent;

					/** The upr. */
					Upr upr;

					/**
					 * Instantiates a new flt.
					 *
					 * @param target
					 *            the target
					 */
					Flt(Sys target) {

						this.outerparent = target;
						this.setLayout(new GridLayout(2, 1, 5, 5));

						this.upr = new Upr(this.outerparent);
						this.lwr = new Lwr(this.outerparent);

						this.add(this.upr);
						this.add(this.lwr);
					}
				} // Flt

				/** The Constant serialVersionUID. */
				private static final long serialVersionUID = 1L;

				/** The cyl. */
				Cyl cyl;

				/** The flt. */
				Flt flt;

				/** The outerparent. */
				Sys outerparent;

				/**
				 * Instantiates a new inp.
				 *
				 * @param target
				 *            the target
				 */
				ProcessInput(Sys target) {
					this.outerparent = target;
					ProcessCard.this.layin = new CardLayout();
					this.setLayout(ProcessCard.this.layin);

					this.flt = new Flt(this.outerparent);
					this.cyl = new Cyl(this.outerparent);

					this.add(MainPanel.FIRST_CARD, this.flt);
					this.add(MainPanel.FOURTH_CARD, this.cyl);
				}
			} // Inp

			/** The Class Oup. */
			class ProcessOutput extends Panel {

				/** The Class Plt2. */
				class ProcessPlotter2 extends Canvas implements Runnable {

					/** The Constant serialVersionUID. */
					private static final long serialVersionUID = 1L;

					/** The ancp. */
					Point locp;
					// private Point ancp;

					/** The outerparent. */
					Sys outerparent;

					/** The run 3. */
					Thread run3;

					/**
					 * Instantiates a new plt 2.
					 *
					 * @param target
					 *            the target
					 */
					ProcessPlotter2(Sys target) {
						this.setBackground(Color.blue);
						this.run3 = null;
					}

					/**
					 * Handleb.
					 *
					 * @param x
					 *            the x
					 * @param y
					 *            the y
					 */
					public void handleb(int x, int y) {
						if (y >= 300) {

							/*
							 * if (x >= 5 && x <= 55) { // rescale endy = 0.0 ; begy = 0.0 ; calcrange = 0 ;
							 * computeFlow() ; } if (x >= 82 && x <= 232) { dispp = 0 ; calcrange = 0 ;
							 * computeFlow() ; } if (x >= 240 && x <= 390) { dispp = 1 ; calcrange = 0 ;
							 * computeFlow() ; }
							 */
						}
						ProcessOutput.this.plt2.repaint();
					}

					/**
					 * (non-Javadoc).
					 *
					 * @param evt
					 *            the evt
					 * @param x
					 *            the x
					 * @param y
					 *            the y
					 * @return true, if successful
					 * @see java.awt.Component#mouseUp(java.awt.Event, int, int)
					 */
					@Override
					public boolean mouseUp(Event evt, int x, int y) {
						this.handleb(x, y);
						return true;
					}

					/**
					 * (non-Javadoc).
					 *
					 * @param g
					 *            the g
					 * @see java.awt.Canvas#paint(java.awt.Graphics)
					 */
					@Override
					public void paint(Graphics g) {
						final boolean b = true;
						if (b && Sys.this.mainPannel.processCard.isVisible()) {
							int i;
							int xlabel, ylabel, ind;
							final int exes[] = new int[8];
							final int whys[] = new int[8];
							double offx;
							double scalex;
							double offy;
							double scaley;
							double incy;
							double incx;
							double xl;
							double yl;
							int pltdata;
							pltdata = 0;
							if (ProcessCard.this.dispp <= 1) {
								Sys.this.processPlatter2ImgBuffGraphContext.setColor(Color.black);
								Sys.this.processPlatter2ImgBuffGraphContext.fillRect(0, 0, Sys.PROCESS_PLOTTER2_WIDTH,
										Sys.PROCESS_PLOTTER2_HEIGHT);
								/*
								 * off8Gg.setColor(Color.white) ; off8Gg.fillRect(2,302,70,15) ;
								 * off8Gg.setColor(Color.red) ; off8Gg.drawString("Rescale",8,315) ;
								 */
								Sys.this.processPlatter2ImgBuffGraphContext.setColor(Color.lightGray);
								Sys.this.processPlatter2ImgBuffGraphContext.fillRect(0, 295, 500, 50);
								Sys.this.processPlatter2ImgBuffGraphContext.setColor(Color.white);
								if (ProcessCard.this.dispp == 0) {
									Sys.this.processPlatter2ImgBuffGraphContext.setColor(Color.yellow);
								}
								Sys.this.processPlatter2ImgBuffGraphContext.fillRect(82, 302, 150, 20);
								Sys.this.processPlatter2ImgBuffGraphContext.setColor(Color.black);
								Sys.this.processPlatter2ImgBuffGraphContext.drawString("Surface Pressure", 88, 317);

								Sys.this.processPlatter2ImgBuffGraphContext.setColor(Color.white);
								if (ProcessCard.this.dispp == 1) {
									Sys.this.processPlatter2ImgBuffGraphContext.setColor(Color.yellow);
								}
								Sys.this.processPlatter2ImgBuffGraphContext.fillRect(240, 302, 150, 20);
								Sys.this.processPlatter2ImgBuffGraphContext.setColor(Color.black);
								Sys.this.processPlatter2ImgBuffGraphContext.drawString(Sys.VELOCITY, 288, 317);
							}
							if (ProcessCard.this.dispp > 1 && ProcessCard.this.dispp <= 15) {
								Sys.this.processPlatter2ImgBuffGraphContext.setColor(Color.blue);
								Sys.this.processPlatter2ImgBuffGraphContext.fillRect(0, 0, 500, 500);
								/*
								 * off8Gg.setColor(Color.white) ; off8Gg.fillRect(2,302,70,15) ;
								 * off8Gg.setColor(Color.red) ; off8Gg.drawString("Rescale",8,315) ;
								 */
							}

							if (ProcessCard.this.ntikx < 2) {
								ProcessCard.this.ntikx = 2; /* protection 13June96 */
							}
							if (ProcessCard.this.ntiky < 2) {
								ProcessCard.this.ntiky = 2;
							}
							offx = 0.0 - ProcessCard.this.begx;
							scalex = 6.0 / (ProcessCard.this.endx - ProcessCard.this.begx);
							incx = (ProcessCard.this.endx - ProcessCard.this.begx) / (ProcessCard.this.ntikx - 1);
							offy = 0.0 - ProcessCard.this.begy;
							scaley = 4.5 / (ProcessCard.this.endy - ProcessCard.this.begy);
							incy = (ProcessCard.this.endy - ProcessCard.this.begy) / (ProcessCard.this.ntiky - 1);

							if (ProcessCard.this.dispp <= 15) { /* draw a graph */
								/* draw axes */
								Sys.this.processPlatter2ImgBuffGraphContext.setColor(Color.white);
								exes[0] = (int) (ProcessCard.this.factp * 0.0) + ProcessCard.this.xtp;
								whys[0] = (int) (ProcessCard.this.factp * -4.5) + ProcessCard.this.ytp;
								exes[1] = (int) (ProcessCard.this.factp * 0.0) + ProcessCard.this.xtp;
								whys[1] = (int) (ProcessCard.this.factp * 0.0) + ProcessCard.this.ytp;
								exes[2] = (int) (ProcessCard.this.factp * 6.0) + ProcessCard.this.xtp;
								whys[2] = (int) (ProcessCard.this.factp * 0.0) + ProcessCard.this.ytp;
								Sys.this.processPlatter2ImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1],
										whys[1]);
								Sys.this.processPlatter2ImgBuffGraphContext.drawLine(exes[1], whys[1], exes[2],
										whys[2]);

								xlabel = (int) -90.0 + ProcessCard.this.xtp; /* label y axis */
								ylabel = (int) (ProcessCard.this.factp * -1.5) + ProcessCard.this.ytp;
								Sys.this.processPlatter2ImgBuffGraphContext.drawString(ProcessCard.this.laby, xlabel,
										ylabel);
								Sys.this.processPlatter2ImgBuffGraphContext.drawString(ProcessCard.this.labyu, xlabel,
										ylabel + 10);
								/* add tick values */
								for (ind = 1; ind <= ProcessCard.this.ntiky; ++ind) {
									xlabel = (int) -50.0 + ProcessCard.this.xtp;
									yl = ProcessCard.this.begy + (ind - 1) * incy;
									ylabel = (int) (ProcessCard.this.factp * -scaley * (yl + offy))
											+ ProcessCard.this.ytp;
									if (ProcessCard.this.nord >= 2) {
										Sys.this.processPlatter2ImgBuffGraphContext.drawString(String.valueOf((int) yl),
												xlabel, ylabel);
									} else {
										Sys.this.processPlatter2ImgBuffGraphContext
												.drawString(String.valueOf(Sys.filter3(yl)), xlabel, ylabel);
									}
								}
								xlabel = (int) (ProcessCard.this.factp * 3.0) + ProcessCard.this.xtp; /* label x axis */
								ylabel = (int) 40.0 + ProcessCard.this.ytp;
								Sys.this.processPlatter2ImgBuffGraphContext.drawString(ProcessCard.this.labx, xlabel,
										ylabel - 10);
								Sys.this.processPlatter2ImgBuffGraphContext.drawString(ProcessCard.this.labxu, xlabel,
										ylabel);
								/* add tick values */
								for (ind = 1; ind <= ProcessCard.this.ntikx; ++ind) {
									ylabel = (int) 15. + ProcessCard.this.ytp;
									xl = ProcessCard.this.begx + (ind - 1) * incx;
									xlabel = (int) (ProcessCard.this.factp * (scalex * (xl + offx) - .05))
											+ ProcessCard.this.xtp;
									if (ProcessCard.this.nabs == 1) {
										Sys.this.processPlatter2ImgBuffGraphContext.drawString(String.valueOf(xl),
												xlabel, ylabel);
									}
									if (ProcessCard.this.nabs > 1) {
										Sys.this.processPlatter2ImgBuffGraphContext.drawString(String.valueOf((int) xl),
												xlabel, ylabel);
									}
								}

								// put a header
								exes[0] = 10;
								whys[0] = 10;
								exes[1] = 50;
								whys[1] = 10;
								Sys.this.processPlatter2ImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1],
										whys[1]);
								Sys.this.processPlatter2ImgBuffGraphContext.drawString("Theory", 60, 15);

								Sys.this.processPlatter2ImgBuffGraphContext.drawString("*", 150, 20);
								Sys.this.processPlatter2ImgBuffGraphContext.drawString("Data", 160, 15);

								Sys.this.processPlatter2ImgBuffGraphContext.setColor(Color.red);
								Sys.this.processPlatter2ImgBuffGraphContext.fillOval(220, 7, 5, 5);
								Sys.this.processPlatter2ImgBuffGraphContext.setColor(Color.white);
								Sys.this.processPlatter2ImgBuffGraphContext.drawString("Diagnostic Point", 230, 15);

								// draw plot
								if (ProcessCard.this.lines == 0) {
									for (i = 1; i <= ProcessCard.this.npt; ++i) {
										xlabel = (int) (ProcessCard.this.factp * scalex
												* (offx + ProcessCard.this.pltx[0][i])) + ProcessCard.this.xtp;
										ylabel = (int) (ProcessCard.this.factp * -scaley
												* (offy + ProcessCard.this.plty[0][i]) + 7.) + ProcessCard.this.ytp;
										Sys.this.processPlatter2ImgBuffGraphContext.drawString("*", xlabel, ylabel);
									}
								} else {
									if (ProcessCard.this.dispp > 1) {
										Sys.this.processPlatter2ImgBuffGraphContext.setColor(Color.white);
										exes[1] = (int) (ProcessCard.this.factp * scalex
												* (offx + ProcessCard.this.pltx[0][1])) + ProcessCard.this.xtp;
										whys[1] = (int) (ProcessCard.this.factp * -scaley
												* (offy + ProcessCard.this.plty[0][1])) + ProcessCard.this.ytp;
										for (i = 1; i <= ProcessCard.this.npt; ++i) {
											exes[0] = exes[1];
											whys[0] = whys[1];
											exes[1] = (int) (ProcessCard.this.factp * scalex
													* (offx + ProcessCard.this.pltx[0][i])) + ProcessCard.this.xtp;
											whys[1] = (int) (ProcessCard.this.factp * -scaley
													* (offy + ProcessCard.this.plty[0][i])) + ProcessCard.this.ytp;
											Sys.this.processPlatter2ImgBuffGraphContext.drawLine(exes[0], whys[0],
													exes[1], whys[1]);
										}
									}
									// test data
									if (ProcessCard.this.dispp == 9
											&& ProcessCard.this.testp[ProcessCard.this.tstflag] == 3) {
										pltdata = 1;
									}
									if (ProcessCard.this.dispp == 2
											&& ProcessCard.this.testp[ProcessCard.this.tstflag] == 2) {
										pltdata = 1;
									}
									if (ProcessCard.this.dispp == 5
											&& ProcessCard.this.testp[ProcessCard.this.tstflag] == 1) {
										pltdata = 1;
									}

									if (pltdata == 1) {
										for (i = 1; i <= ProcessCard.this.numpt[ProcessCard.this.tstflag]; ++i) {
											xlabel = (int) (ProcessCard.this.factp * scalex
													* (offx + ProcessCard.this.plttx[1][i])) + ProcessCard.this.xtp;
											ylabel = (int) (ProcessCard.this.factp * -scaley
													* (offy + ProcessCard.this.pltty[1][i]) + 7.)
													+ ProcessCard.this.ytp;
											Sys.this.processPlatter2ImgBuffGraphContext.drawString("*", xlabel, ylabel);
										}
									}
									xlabel = (int) (ProcessCard.this.factp * scalex
											* (offx + ProcessCard.this.pltx[1][0])) + ProcessCard.this.xtp;
									ylabel = (int) (ProcessCard.this.factp * -scaley
											* (offy + ProcessCard.this.plty[1][0])) + ProcessCard.this.ytp - 4;
									Sys.this.processPlatter2ImgBuffGraphContext.setColor(Color.red);
									Sys.this.processPlatter2ImgBuffGraphContext.fillOval(xlabel, ylabel, 5, 5);
								}
							}

							g.drawImage(Sys.this.processPlotter2ImageBuffer, 0, 0, this);
						}
					}

					/**
					 * (non-Javadoc).
					 *
					 * @see java.lang.Runnable#run()
					 */
					@Override
					public void run() {
						int timer;

						timer = 100;
						while (true) {
							try {
								Thread.sleep(timer);
							} catch (final InterruptedException e) {
							}
							ProcessOutput.this.plt2.repaint();
						}
					}

					/** Start. */
					public void start() {
						if (this.run3 == null) {
							this.run3 = new Thread(this);
							this.run3.start();
						}
						Sys.logger.info("Oup Process Plotter 2 started");
					}

					/**
					 * (non-Javadoc).
					 *
					 * @param g
					 *            the g
					 * @see java.awt.Canvas#update(java.awt.Graphics)
					 */
					@Override
					public void update(Graphics g) {
						ProcessOutput.this.plt2.paint(g);
					}
				} // Plt2

				/** The Class Viewer. */
				class ProcessViewer extends Canvas implements Runnable {

					/** The Constant serialVersionUID. */
					private static final long serialVersionUID = 1L;

					/** The anchor. */
					// private Point locate;
					// private Point anchor;

					/** The outerparent. */
					// private Sys outerparent;

					/** The runner. */
					private Thread runner;

					/**
					 * Instantiates a new viewer.
					 *
					 * @param target
					 *            the target
					 */
					ProcessViewer(Sys target) {
						this.setBackground(Color.black);
						this.runner = null;
					}

					/**
					 * Handle.
					 *
					 * @param x
					 *            the x
					 * @param y
					 *            the y
					 */
					public void handle(int x, int y) {
						// determine location
						if (y >= 30) {

						}
					}

					/**
					 * Handleb.
					 *
					 * @param x
					 *            the x
					 * @param y
					 *            the y
					 */
					public void handleb(int x, int y) {
						if (y >= 300) {
							if (x >= 82 && x <= 232) { // transparent wall
								ProcessCard.this.viewflg = WALL_VIEW_TRANSPARENT;
							}
							if (x >= 240 && x <= 390) { // solid wall
								ProcessCard.this.viewflg = WALL_VIEW_SOLID;
							}
						}
						ProcessOutput.this.view.repaint();
					}

					/**
					 * Insets.
					 *
					 * @return the insets
					 */
					public Insets insets() {
						return new Insets(0, 5, 0, 5);
					}

					/**
					 * (non-Javadoc).
					 *
					 * @param evt
					 *            the evt
					 * @param x
					 *            the x
					 * @param y
					 *            the y
					 * @return true, if successful
					 * @see java.awt.Component#mouseDown(java.awt.Event, int, int)
					 */
					@Override
					public boolean mouseDown(Event evt, int x, int y) {
						// this.anchor = new Point(x, y);
						return true;
					}

					/**
					 * (non-Javadoc).
					 *
					 * @param evt
					 *            the evt
					 * @param x
					 *            the x
					 * @param y
					 *            the y
					 * @return true, if successful
					 * @see java.awt.Component#mouseDrag(java.awt.Event, int, int)
					 */
					@Override
					public boolean mouseDrag(Event evt, int x, int y) {
						this.handle(x, y);
						return true;
					}

					/**
					 * (non-Javadoc).
					 *
					 * @param evt
					 *            the evt
					 * @param x
					 *            the x
					 * @param y
					 *            the y
					 * @return true, if successful
					 * @see java.awt.Component#mouseUp(java.awt.Event, int, int)
					 */
					@Override
					public boolean mouseUp(Event evt, int x, int y) {
						this.handleb(x, y);
						return true;
					}

					/**
					 * (non-Javadoc).
					 *
					 * @param g
					 *            the g
					 * @see java.awt.Canvas#paint(java.awt.Graphics)
					 */
					@Override
					public void paint(Graphics g) {
						final boolean b = true;
						if (b && Sys.this.mainPannel.processCard.isVisible()) {
							int i, j;
							int n;
							int inmax;
							final int exes[] = new int[8];
							final int whys[] = new int[8];
							double slope, radvec, xvec, yvec;
							double yprs, yprs1;
							final int camx[] = new int[40];
							final int camy[] = new int[40];
							Color col;

							col = new Color(0, 0, 0);
							if (ProcessCard.this.planet == 0) {
								col = Color.cyan;
							}
							if (ProcessCard.this.planet == 1) {
								col = Color.orange;
							}
							if (ProcessCard.this.planet == 2) {
								col = Color.green;
							}
							if (ProcessCard.this.planet >= 3) {
								col = Color.cyan;
							}
							Sys.this.processViewerImgBufGraphContext.setColor(Color.lightGray);
							Sys.this.processViewerImgBufGraphContext.fillRect(0, 0, Sys.PROCESS_VIEWER_WIDTH,
									Sys.PROCESS_VIEWER_HEIGHT);
							Sys.this.processViewerImgBufGraphContext.setColor(Color.black);
							exes[0] = 0;
							whys[0] = 200;
							exes[1] = 500;
							whys[1] = 250;
							exes[2] = 500;
							whys[2] = 500;
							exes[3] = 0;
							whys[3] = 500;
							Sys.this.processViewerImgBufGraphContext.fillPolygon(exes, whys, 4);

							radvec = .5;

							if (ProcessCard.this.viewflg == WALL_VIEW_TRANSPARENT
									|| ProcessCard.this.viewflg == WALL_VIEW_SOLID) { // edge View
								if (ProcessCard.this.vfsd > .01) {
									/* plot airfoil flowfield */
									for (j = 1; j <= ProcessCard.this.nln2 - 1; ++j) { /* lower half */
										for (i = 1; i <= ProcessCard.this.nptc - 1; ++i) {
											exes[0] = (int) (ProcessCard.this.fact * ProcessCard.this.xpl[j][i])
													+ ProcessCard.this.xt;
											yprs = .1 * ProcessCard.this.xpl[j][i];
											yprs1 = .1 * ProcessCard.this.xpl[j][i + 1];
											whys[0] = (int) (ProcessCard.this.fact
													* (-ProcessCard.this.ypl[j][i] + yprs)) + ProcessCard.this.yt;
											slope = (ProcessCard.this.ypl[j][i + 1] - yprs1 - ProcessCard.this.ypl[j][i]
													+ yprs)
													/ (ProcessCard.this.xpl[j][i + 1] - ProcessCard.this.xpl[j][i]);
											xvec = ProcessCard.this.xpl[j][i] + radvec / Math.sqrt(1.0 + slope * slope);
											yvec = ProcessCard.this.ypl[j][i] - yprs
													+ slope * (xvec - ProcessCard.this.xpl[j][i]);
											exes[1] = (int) (ProcessCard.this.fact * xvec) + ProcessCard.this.xt;
											whys[1] = (int) (ProcessCard.this.fact * -yvec) + ProcessCard.this.yt;

											if (ProcessCard.this.displ == 0) { /* MODS 21 JUL 99 */
												Sys.this.processViewerImgBufGraphContext.setColor(Color.yellow);
												exes[1] = (int) (ProcessCard.this.fact * ProcessCard.this.xpl[j][i + 1])
														+ ProcessCard.this.xt;
												yprs = .1 * ProcessCard.this.xpl[j][i + 1];
												whys[1] = (int) (ProcessCard.this.fact
														* (-ProcessCard.this.ypl[j][i + 1] + yprs))
														+ ProcessCard.this.yt;
												Sys.this.processViewerImgBufGraphContext.drawLine(exes[0], whys[0],
														exes[1], whys[1]);
											}

											if (ProcessCard.this.displ == 2 && i / 3 * 3 == i) {
												Sys.this.processViewerImgBufGraphContext.setColor(col);
												for (n = 1; n <= 4; ++n) {
													if (i == 6 + (n - 1) * 9) {
														Sys.this.processViewerImgBufGraphContext.setColor(Color.yellow);
													}
												}

												if (i / 9 * 9 == i) {
													Sys.this.processViewerImgBufGraphContext.setColor(Color.white);
												}
												Sys.this.processViewerImgBufGraphContext.drawLine(exes[0], whys[0],
														exes[1], whys[1]);
											}
											if (ProcessCard.this.displ == 1 && (i - ProcessCard.this.antim) / 3 * 3 == i
													- ProcessCard.this.antim) {
												if (ProcessCard.this.ancol == -1) { /* MODS 27 JUL 99 */
													if ((i - ProcessCard.this.antim) / 6 * 6 == i
															- ProcessCard.this.antim) {
														Sys.this.processViewerImgBufGraphContext.setColor(col);
													}
													if ((i - ProcessCard.this.antim) / 6 * 6 != i
															- ProcessCard.this.antim) {
														Sys.this.processViewerImgBufGraphContext.setColor(Color.white);
													}
												}
												if (ProcessCard.this.ancol == 1) { /* MODS 27 JUL 99 */
													if ((i - ProcessCard.this.antim) / 6 * 6 == i
															- ProcessCard.this.antim) {
														Sys.this.processViewerImgBufGraphContext.setColor(Color.white);
													}
													if ((i - ProcessCard.this.antim) / 6 * 6 != i
															- ProcessCard.this.antim) {
														Sys.this.processViewerImgBufGraphContext.setColor(col);
													}
												}
												Sys.this.processViewerImgBufGraphContext.drawLine(exes[0], whys[0],
														exes[1], whys[1]);
											}
										}
									}

									Sys.this.processViewerImgBufGraphContext.setColor(Color.white); /* stagnation */
									exes[1] = (int) (ProcessCard.this.fact
											* ProcessCard.this.xpl[ProcessCard.this.nln2][1]) + ProcessCard.this.xt;
									yprs = .1 * ProcessCard.this.xpl[ProcessCard.this.nln2][1];
									whys[1] = (int) (ProcessCard.this.fact
											* (-ProcessCard.this.ypl[ProcessCard.this.nln2][1] + yprs))
											+ ProcessCard.this.yt;
									for (i = 2; i <= ProcessCard.this.npt2 - 1; ++i) {
										exes[0] = exes[1];
										whys[0] = whys[1];
										exes[1] = (int) (ProcessCard.this.fact
												* ProcessCard.this.xpl[ProcessCard.this.nln2][i]) + ProcessCard.this.xt;
										yprs = .1 * ProcessCard.this.xpl[ProcessCard.this.nln2][i];
										whys[1] = (int) (ProcessCard.this.fact
												* (-ProcessCard.this.ypl[ProcessCard.this.nln2][i] + yprs))
												+ ProcessCard.this.yt;
										if (ProcessCard.this.displ <= 2) { /* MODS 21 JUL 99 */
											Sys.this.processViewerImgBufGraphContext.drawLine(exes[0], whys[0], exes[1],
													whys[1]);
										}
									}
									exes[1] = (int) (ProcessCard.this.fact
											* ProcessCard.this.xpl[ProcessCard.this.nln2][ProcessCard.this.npt2 + 1])
											+ ProcessCard.this.xt;
									yprs = .1 * ProcessCard.this.xpl[ProcessCard.this.nln2][ProcessCard.this.npt2 + 1];
									whys[1] = (int) (ProcessCard.this.fact
											* (-ProcessCard.this.ypl[ProcessCard.this.nln2][ProcessCard.this.npt2 + 1]
													+ yprs))
											+ ProcessCard.this.yt;
									for (i = ProcessCard.this.npt2 + 2; i <= ProcessCard.this.nptc; ++i) {
										exes[0] = exes[1];
										whys[0] = whys[1];
										exes[1] = (int) (ProcessCard.this.fact
												* ProcessCard.this.xpl[ProcessCard.this.nln2][i]) + ProcessCard.this.xt;
										yprs = .1 * ProcessCard.this.xpl[ProcessCard.this.nln2][i];
										whys[1] = (int) (ProcessCard.this.fact
												* (-ProcessCard.this.ypl[ProcessCard.this.nln2][i] + yprs))
												+ ProcessCard.this.yt;
										if (ProcessCard.this.displ <= 2) { /* MODS 21 JUL 99 */
											Sys.this.processViewerImgBufGraphContext.drawLine(exes[0], whys[0], exes[1],
													whys[1]);
										}
									}
								}
								/* probe location */
								probeLocation(exes, whys, radvec);

								// wing surface
								if (ProcessCard.this.viewflg == WALL_VIEW_SOLID) { // 3d geom
									exes[1] = (int) (ProcessCard.this.fact
											* ProcessCard.this.xpl[0][ProcessCard.this.npt2]) + ProcessCard.this.xt1;
									whys[1] = (int) (ProcessCard.this.fact
											* -ProcessCard.this.ypl[0][ProcessCard.this.npt2]) + ProcessCard.this.yt1;
									exes[2] = (int) (ProcessCard.this.fact
											* ProcessCard.this.xpl[0][ProcessCard.this.npt2]) + ProcessCard.this.xt2;
									whys[2] = (int) (ProcessCard.this.fact
											* -ProcessCard.this.ypl[0][ProcessCard.this.npt2]) + ProcessCard.this.yt2;
									for (i = 1; i <= ProcessCard.this.npt2 - 1; ++i) {
										exes[0] = exes[1];
										whys[0] = whys[1];
										exes[1] = (int) (ProcessCard.this.fact
												* ProcessCard.this.xpl[0][ProcessCard.this.npt2 - i])
												+ ProcessCard.this.xt1;
										whys[1] = (int) (ProcessCard.this.fact
												* -ProcessCard.this.ypl[0][ProcessCard.this.npt2 - i])
												+ ProcessCard.this.yt1;
										exes[3] = exes[2];
										whys[3] = whys[2];
										exes[2] = (int) (ProcessCard.this.fact
												* ProcessCard.this.xpl[0][ProcessCard.this.npt2 - i])
												+ ProcessCard.this.xt2;
										whys[2] = (int) (ProcessCard.this.fact
												* -ProcessCard.this.ypl[0][ProcessCard.this.npt2 - i])
												+ ProcessCard.this.yt2;
										Sys.this.processViewerImgBufGraphContext.setColor(Color.red);
										Sys.this.processViewerImgBufGraphContext.fillPolygon(exes, whys, 4);
										Sys.this.processViewerImgBufGraphContext.setColor(Color.black);
										Sys.this.processViewerImgBufGraphContext.drawLine(exes[1], whys[1], exes[2],
												whys[2]);
									}
								}
								if (ProcessCard.this.vfsd > .01) {
									for (j = ProcessCard.this.nln2
											+ 1; j <= ProcessCard.this.nlnc; ++j) { /* upper half */
										for (i = 1; i <= ProcessCard.this.nptc - 1; ++i) {
											exes[0] = (int) (ProcessCard.this.fact * ProcessCard.this.xpl[j][i])
													+ ProcessCard.this.xt;
											yprs = .1 * ProcessCard.this.xpl[j][i];
											yprs1 = .1 * ProcessCard.this.xpl[j][i + 1];
											whys[0] = (int) (ProcessCard.this.fact
													* (-ProcessCard.this.ypl[j][i] + yprs)) + ProcessCard.this.yt;
											slope = (ProcessCard.this.ypl[j][i + 1] - yprs1 - ProcessCard.this.ypl[j][i]
													+ yprs)
													/ (ProcessCard.this.xpl[j][i + 1] - ProcessCard.this.xpl[j][i]);
											xvec = ProcessCard.this.xpl[j][i] + radvec / Math.sqrt(1.0 + slope * slope);
											yvec = ProcessCard.this.ypl[j][i] - yprs
													+ slope * (xvec - ProcessCard.this.xpl[j][i]);
											exes[1] = (int) (ProcessCard.this.fact * xvec) + ProcessCard.this.xt;
											whys[1] = (int) (ProcessCard.this.fact * -yvec) + ProcessCard.this.yt;
											if (ProcessCard.this.displ == 0) { /* MODS 21 JUL 99 */
												Sys.this.processViewerImgBufGraphContext.setColor(col);
												exes[1] = (int) (ProcessCard.this.fact * ProcessCard.this.xpl[j][i + 1])
														+ ProcessCard.this.xt;
												yprs = .1 * ProcessCard.this.xpl[j][1 + 1];
												whys[1] = (int) (ProcessCard.this.fact
														* (-ProcessCard.this.ypl[j][i + 1] + yprs))
														+ ProcessCard.this.yt;
												Sys.this.processViewerImgBufGraphContext.drawLine(exes[0], whys[0],
														exes[1], whys[1]);
											}
											if (ProcessCard.this.displ == 2 && i / 3 * 3 == i) {
												Sys.this.processViewerImgBufGraphContext
														.setColor(col); /* MODS 27 JUL 99 */
												for (n = 1; n <= 4; ++n) {
													if (i == 6 + (n - 1) * 9) {
														Sys.this.processViewerImgBufGraphContext.setColor(Color.yellow);
													}
												}
												if (i / 9 * 9 == i) {
													Sys.this.processViewerImgBufGraphContext.setColor(Color.white);
												}
												Sys.this.processViewerImgBufGraphContext.drawLine(exes[0], whys[0],
														exes[1], whys[1]);
											}
											if (ProcessCard.this.displ == 1 && (i - ProcessCard.this.antim) / 3 * 3 == i
													- ProcessCard.this.antim) {
												if (ProcessCard.this.ancol == -1) { /* MODS 27 JUL 99 */
													if ((i - ProcessCard.this.antim) / 6 * 6 == i
															- ProcessCard.this.antim) {
														Sys.this.processViewerImgBufGraphContext.setColor(col);
													}
													if ((i - ProcessCard.this.antim) / 6 * 6 != i
															- ProcessCard.this.antim) {
														Sys.this.processViewerImgBufGraphContext.setColor(Color.white);
													}
												}
												if (ProcessCard.this.ancol == 1) { /* MODS 27 JUL 99 */
													if ((i - ProcessCard.this.antim) / 6 * 6 == i
															- ProcessCard.this.antim) {
														Sys.this.processViewerImgBufGraphContext.setColor(Color.white);
													}
													if ((i - ProcessCard.this.antim) / 6 * 6 != i
															- ProcessCard.this.antim) {
														Sys.this.processViewerImgBufGraphContext.setColor(col);
													}
												}
												Sys.this.processViewerImgBufGraphContext.drawLine(exes[0], whys[0],
														exes[1], whys[1]);
											}
										}
									}
								}
								if (ProcessCard.this.pboflag > PROBE_OFF && ProcessCard.this.pypl > 0.0) {
									Sys.this.processViewerImgBufGraphContext.setColor(Color.magenta);
									yprs = .1 * ProcessCard.this.pxpl;
									Sys.this.processViewerImgBufGraphContext.fillOval(
											(int) (ProcessCard.this.fact * ProcessCard.this.pxpl) + ProcessCard.this.xt,
											(int) (ProcessCard.this.fact * (-ProcessCard.this.pypl + yprs))
													+ ProcessCard.this.yt - 2,
											5, 5);
									Sys.this.processViewerImgBufGraphContext.setColor(Color.white);
									exes[0] = (int) (ProcessCard.this.fact * (ProcessCard.this.pxpl + .1))
											+ ProcessCard.this.xt;
									whys[0] = (int) (ProcessCard.this.fact * (-ProcessCard.this.pypl + yprs))
											+ ProcessCard.this.yt;
									exes[1] = (int) (ProcessCard.this.fact * (ProcessCard.this.pxpl + .5))
											+ ProcessCard.this.xt;
									whys[1] = (int) (ProcessCard.this.fact * (-ProcessCard.this.pypl + yprs))
											+ ProcessCard.this.yt;
									exes[2] = (int) (ProcessCard.this.fact * (ProcessCard.this.pxpl + .5))
											+ ProcessCard.this.xt;
									whys[2] = (int) (ProcessCard.this.fact * (-ProcessCard.this.pypl - 50.))
											+ ProcessCard.this.yt;
									Sys.this.processViewerImgBufGraphContext.drawLine(exes[0], whys[0], exes[1],
											whys[1]);
									Sys.this.processViewerImgBufGraphContext.drawLine(exes[1], whys[1], exes[2],
											whys[2]);
									if (ProcessCard.this.pboflag == PROBLE_SMOKE && ProcessCard.this.vfsd >= .01) { // smoke
																													// trail
										Sys.this.processViewerImgBufGraphContext.setColor(Color.green);
										for (i = 1; i <= ProcessCard.this.nptc - 1; ++i) {
											exes[0] = (int) (ProcessCard.this.fact * ProcessCard.this.xpl[19][i])
													+ ProcessCard.this.xt;
											yprs = .1 * ProcessCard.this.xpl[19][i];
											yprs1 = .1 * ProcessCard.this.xpl[19][i + 1];
											whys[0] = (int) (ProcessCard.this.fact
													* (-ProcessCard.this.ypl[19][i] + yprs)) + ProcessCard.this.yt;
											slope = (ProcessCard.this.ypl[19][i + 1] - yprs1
													- ProcessCard.this.ypl[19][i] + yprs)
													/ (ProcessCard.this.xpl[19][i + 1] - ProcessCard.this.xpl[19][i]);
											xvec = ProcessCard.this.xpl[19][i]
													+ radvec / Math.sqrt(1.0 + slope * slope);
											yvec = ProcessCard.this.ypl[19][i] - yprs
													+ slope * (xvec - ProcessCard.this.xpl[19][i]);
											exes[1] = (int) (ProcessCard.this.fact * xvec) + ProcessCard.this.xt;
											whys[1] = (int) (ProcessCard.this.fact * -yvec) + ProcessCard.this.yt;
											Sys.this.processViewerImgBufGraphContext.drawLine(exes[0], whys[0], exes[1],
													whys[1]);
											/*
											 * if ((i-antim)/3*3 == (i-antim) ) {
											 * off6Gg.drawLine(exes[0],whys[0],exes[1],whys[1]) ; }
											 */
										}
									}
								}

								if (ProcessCard.this.viewflg == WALL_VIEW_TRANSPARENT) {
									// front foil
									Sys.this.processViewerImgBufGraphContext.setColor(Color.white);
									exes[1] = (int) (ProcessCard.this.fact
											* ProcessCard.this.xpl[0][ProcessCard.this.npt2]) + ProcessCard.this.xt2;
									whys[1] = (int) (ProcessCard.this.fact
											* -ProcessCard.this.ypl[0][ProcessCard.this.npt2]) + ProcessCard.this.yt2;
									exes[2] = (int) (ProcessCard.this.fact
											* ProcessCard.this.xpl[0][ProcessCard.this.npt2]) + ProcessCard.this.xt2;
									whys[2] = (int) (ProcessCard.this.fact
											* -ProcessCard.this.ypl[0][ProcessCard.this.npt2]) + ProcessCard.this.yt2;
									for (i = 1; i <= ProcessCard.this.npt2 - 1; ++i) {
										exes[0] = exes[1];
										whys[0] = whys[1];
										exes[1] = (int) (ProcessCard.this.fact
												* ProcessCard.this.xpl[0][ProcessCard.this.npt2 - i])
												+ ProcessCard.this.xt2;
										whys[1] = (int) (ProcessCard.this.fact
												* -ProcessCard.this.ypl[0][ProcessCard.this.npt2 - i])
												+ ProcessCard.this.yt2;
										exes[3] = exes[2];
										whys[3] = whys[2];
										exes[2] = (int) (ProcessCard.this.fact
												* ProcessCard.this.xpl[0][ProcessCard.this.npt2 + i])
												+ ProcessCard.this.xt2;
										whys[2] = (int) (ProcessCard.this.fact
												* -ProcessCard.this.ypl[0][ProcessCard.this.npt2 + i])
												+ ProcessCard.this.yt2;
										camx[i] = (exes[1] + exes[2]) / 2;
										camy[i] = (whys[1] + whys[2]) / 2;
										Sys.this.processViewerImgBufGraphContext.fillPolygon(exes, whys, 4);
									}
									// middl e airfoil geometry
									Sys.this.processViewerImgBufGraphContext.setColor(Color.white);
									exes[1] = (int) (ProcessCard.this.fact
											* ProcessCard.this.xpl[0][ProcessCard.this.npt2]) + ProcessCard.this.xt;
									whys[1] = (int) (ProcessCard.this.fact
											* -ProcessCard.this.ypl[0][ProcessCard.this.npt2]) + ProcessCard.this.yt;
									exes[2] = (int) (ProcessCard.this.fact
											* ProcessCard.this.xpl[0][ProcessCard.this.npt2]) + ProcessCard.this.xt;
									whys[2] = (int) (ProcessCard.this.fact
											* -ProcessCard.this.ypl[0][ProcessCard.this.npt2]) + ProcessCard.this.yt;
									for (i = 1; i <= ProcessCard.this.npt2 - 1; ++i) {
										exes[0] = exes[1];
										whys[0] = whys[1];
										exes[1] = (int) (ProcessCard.this.fact
												* ProcessCard.this.xpl[0][ProcessCard.this.npt2 - i])
												+ ProcessCard.this.xt;
										whys[1] = (int) (ProcessCard.this.fact
												* -ProcessCard.this.ypl[0][ProcessCard.this.npt2 - i])
												+ ProcessCard.this.yt;
										exes[3] = exes[2];
										whys[3] = whys[2];
										exes[2] = (int) (ProcessCard.this.fact
												* ProcessCard.this.xpl[0][ProcessCard.this.npt2 + i])
												+ ProcessCard.this.xt;
										whys[2] = (int) (ProcessCard.this.fact
												* -ProcessCard.this.ypl[0][ProcessCard.this.npt2 + i])
												+ ProcessCard.this.yt;
										camx[i] = (exes[1] + exes[2]) / 2;
										camy[i] = (whys[1] + whys[2]) / 2;
										if (ProcessCard.this.foiltype == FOILTYPE_PLATE) {
											Sys.this.processViewerImgBufGraphContext.setColor(Color.yellow);
											Sys.this.processViewerImgBufGraphContext.drawLine(exes[0], whys[0], exes[1],
													whys[1]);
										} else {
											Sys.this.processViewerImgBufGraphContext.setColor(Color.white);
											Sys.this.processViewerImgBufGraphContext.fillPolygon(exes, whys, 4);
										}
									}
									// back foil
									Sys.this.processViewerImgBufGraphContext.setColor(Color.white);
									exes[1] = (int) (ProcessCard.this.fact
											* ProcessCard.this.xpl[0][ProcessCard.this.npt2]) + ProcessCard.this.xt1;
									whys[1] = (int) (ProcessCard.this.fact
											* -ProcessCard.this.ypl[0][ProcessCard.this.npt2]) + ProcessCard.this.yt1;
									exes[2] = (int) (ProcessCard.this.fact
											* ProcessCard.this.xpl[0][ProcessCard.this.npt2]) + ProcessCard.this.xt1;
									whys[2] = (int) (ProcessCard.this.fact
											* -ProcessCard.this.ypl[0][ProcessCard.this.npt2]) + ProcessCard.this.yt1;
									for (i = 1; i <= ProcessCard.this.npt2 - 1; ++i) {
										exes[0] = exes[1];
										whys[0] = whys[1];
										exes[1] = (int) (ProcessCard.this.fact
												* ProcessCard.this.xpl[0][ProcessCard.this.npt2 - i])
												+ ProcessCard.this.xt1;
										whys[1] = (int) (ProcessCard.this.fact
												* -ProcessCard.this.ypl[0][ProcessCard.this.npt2 - i])
												+ ProcessCard.this.yt1;
										exes[3] = exes[2];
										whys[3] = whys[2];
										exes[2] = (int) (ProcessCard.this.fact
												* ProcessCard.this.xpl[0][ProcessCard.this.npt2 + i])
												+ ProcessCard.this.xt1;
										whys[2] = (int) (ProcessCard.this.fact
												* -ProcessCard.this.ypl[0][ProcessCard.this.npt2 + i])
												+ ProcessCard.this.yt1;
										camx[i] = (exes[1] + exes[2]) / 2;
										camy[i] = (whys[1] + whys[2]) / 2;
										Sys.this.processViewerImgBufGraphContext.fillPolygon(exes, whys, 4);
									}
									// leading and trailing edge
									Sys.this.processViewerImgBufGraphContext.setColor(Color.white);
									exes[1] = (int) (ProcessCard.this.fact
											* ProcessCard.this.xpl[0][ProcessCard.this.npt2]) + ProcessCard.this.xt1;
									whys[1] = (int) (ProcessCard.this.fact
											* -ProcessCard.this.ypl[0][ProcessCard.this.npt2]) + ProcessCard.this.yt1;
									exes[2] = (int) (ProcessCard.this.fact
											* ProcessCard.this.xpl[0][ProcessCard.this.npt2]) + ProcessCard.this.xt2;
									whys[2] = (int) (ProcessCard.this.fact
											* -ProcessCard.this.ypl[0][ProcessCard.this.npt2]) + ProcessCard.this.yt2;
									Sys.this.processViewerImgBufGraphContext.drawLine(exes[1], whys[1], exes[2],
											whys[2]);
									exes[1] = (int) (ProcessCard.this.fact * ProcessCard.this.xpl[0][1])
											+ ProcessCard.this.xt1;
									whys[1] = (int) (ProcessCard.this.fact * -ProcessCard.this.ypl[0][1])
											+ ProcessCard.this.yt1;
									exes[2] = (int) (ProcessCard.this.fact * ProcessCard.this.xpl[0][1])
											+ ProcessCard.this.xt2;
									whys[2] = (int) (ProcessCard.this.fact * -ProcessCard.this.ypl[0][1])
											+ ProcessCard.this.yt2;
									Sys.this.processViewerImgBufGraphContext.drawLine(exes[1], whys[1], exes[2],
											whys[2]);
									// pu t some info on the geometry
									if (ProcessCard.this.displ == 3) {
										if (ProcessCard.this.foiltype <= FOILTYPE_PLATE) {
											inmax = 1;
											for (n = 1; n <= ProcessCard.this.nptc; ++n) {
												if (ProcessCard.this.xpl[0][n] > ProcessCard.this.xpl[0][inmax]) {
													inmax = n;
												}
											}
											Sys.this.processViewerImgBufGraphContext.setColor(Color.green);
											exes[0] = (int) (ProcessCard.this.fact * ProcessCard.this.xpl[0][inmax])
													+ ProcessCard.this.xt;
											whys[0] = (int) (ProcessCard.this.fact * -ProcessCard.this.ypl[0][inmax])
													+ ProcessCard.this.yt;
											Sys.this.processViewerImgBufGraphContext.drawLine(exes[0], whys[0],
													exes[0] - 250, whys[0]);
											Sys.this.processViewerImgBufGraphContext.drawString("Reference", 30,
													whys[0] + 10);
											Sys.this.processViewerImgBufGraphContext.drawString(Sys.ANGLE, exes[0] + 20,
													whys[0]);

											Sys.this.processViewerImgBufGraphContext.setColor(Color.cyan);
											exes[1] = (int) (ProcessCard.this.fact
													* (ProcessCard.this.xpl[0][inmax] - 4.0 * Math
															.cos(ProcessCard.this.convdr * ProcessCard.this.alfval)))
													+ ProcessCard.this.xt;
											whys[1] = (int) (ProcessCard.this.fact
													* (-ProcessCard.this.ypl[0][inmax] - 4.0 * Math
															.sin(ProcessCard.this.convdr * ProcessCard.this.alfval)))
													+ ProcessCard.this.yt;
											Sys.this.processViewerImgBufGraphContext.drawLine(exes[0], whys[0], exes[1],
													whys[1]);
											Sys.this.processViewerImgBufGraphContext.drawString("Chord Line",
													exes[0] + 20, whys[0] + 20);

											Sys.this.processViewerImgBufGraphContext.setColor(Color.red);
											Sys.this.processViewerImgBufGraphContext.drawLine(exes[1], whys[1], camx[5],
													camy[5]);
											for (i = 7; i <= ProcessCard.this.npt2 - 6; i = i + 2) {
												Sys.this.processViewerImgBufGraphContext.drawLine(camx[i], camy[i],
														camx[i + 1], camy[i + 1]);
											}
											Sys.this.processViewerImgBufGraphContext.drawString("Mean Camber Line",
													exes[0] - 70, whys[1] - 10);
										}
										if (ProcessCard.this.foiltype >= 4) {
											Sys.this.processViewerImgBufGraphContext.setColor(Color.red);
											exes[0] = (int) (ProcessCard.this.fact * ProcessCard.this.xpl[0][1])
													+ ProcessCard.this.xt;
											whys[0] = (int) (ProcessCard.this.fact * -ProcessCard.this.ypl[0][1])
													+ ProcessCard.this.yt;
											exes[1] = (int) (ProcessCard.this.fact
													* ProcessCard.this.xpl[0][ProcessCard.this.npt2])
													+ ProcessCard.this.xt;
											whys[1] = (int) (ProcessCard.this.fact
													* -ProcessCard.this.ypl[0][ProcessCard.this.npt2])
													+ ProcessCard.this.yt;
											Sys.this.processViewerImgBufGraphContext.drawLine(exes[0], whys[0], exes[1],
													whys[1]);
											Sys.this.processViewerImgBufGraphContext.drawString(Sys.DIAMETER,
													exes[0] + 20, whys[0] + 20);
										}

										Sys.this.processViewerImgBufGraphContext.setColor(Color.green);
										Sys.this.processViewerImgBufGraphContext.drawString(Sys.FLOW, 30, 145);
										Sys.this.processViewerImgBufGraphContext.drawLine(30, 152, 60, 152);
										exes[0] = 60;
										exes[1] = 60;
										exes[2] = 70;
										whys[0] = 157;
										whys[1] = 147;
										whys[2] = 152;
										Sys.this.processViewerImgBufGraphContext.fillPolygon(exes, whys, 3);
									}
									// spin the cylinder and ball
									if (ProcessCard.this.foiltype >= FOILTYPE_CYLINDER) {
										exes[0] = (int) (ProcessCard.this.fact * (.5
												* (ProcessCard.this.xpl[0][1]
														+ ProcessCard.this.xpl[0][ProcessCard.this.npt2])
												+ ProcessCard.this.rval * Math.cos(
														ProcessCard.this.convdr * (ProcessCard.this.plthg[1] + 180.))))
												+ ProcessCard.this.xt;
										whys[0] = (int) (ProcessCard.this.fact
												* (-ProcessCard.this.ypl[0][1] + ProcessCard.this.rval * Math.sin(
														ProcessCard.this.convdr * (ProcessCard.this.plthg[1] + 180.))))
												+ ProcessCard.this.yt;
										exes[1] = (int) (ProcessCard.this.fact * (.5
												* (ProcessCard.this.xpl[0][1]
														+ ProcessCard.this.xpl[0][ProcessCard.this.npt2])
												+ ProcessCard.this.rval * Math
														.cos(ProcessCard.this.convdr * ProcessCard.this.plthg[1])))
												+ ProcessCard.this.xt;
										whys[1] = (int) (ProcessCard.this.fact
												* (-ProcessCard.this.ypl[0][1] + ProcessCard.this.rval * Math
														.sin(ProcessCard.this.convdr * ProcessCard.this.plthg[1])))
												+ ProcessCard.this.yt;
										Sys.this.processViewerImgBufGraphContext.setColor(Color.red);
										Sys.this.processViewerImgBufGraphContext.drawLine(exes[0], whys[0], exes[1],
												whys[1]);
									}
								}
								if (ProcessCard.this.viewflg == WALL_VIEW_SOLID) {
									// front foil
									Sys.this.processViewerImgBufGraphContext.setColor(Color.white);
									exes[1] = (int) (ProcessCard.this.fact
											* ProcessCard.this.xpl[0][ProcessCard.this.npt2]) + ProcessCard.this.xt2;
									whys[1] = (int) (ProcessCard.this.fact
											* -ProcessCard.this.ypl[0][ProcessCard.this.npt2]) + ProcessCard.this.yt2;
									exes[2] = (int) (ProcessCard.this.fact
											* ProcessCard.this.xpl[0][ProcessCard.this.npt2]) + ProcessCard.this.xt2;
									whys[2] = (int) (ProcessCard.this.fact
											* -ProcessCard.this.ypl[0][ProcessCard.this.npt2]) + ProcessCard.this.yt2;
									for (i = 1; i <= ProcessCard.this.npt2 - 1; ++i) {
										exes[0] = exes[1];
										whys[0] = whys[1];
										exes[1] = (int) (ProcessCard.this.fact
												* ProcessCard.this.xpl[0][ProcessCard.this.npt2 - i])
												+ ProcessCard.this.xt2;
										whys[1] = (int) (ProcessCard.this.fact
												* -ProcessCard.this.ypl[0][ProcessCard.this.npt2 - i])
												+ ProcessCard.this.yt2;
										exes[3] = exes[2];
										whys[3] = whys[2];
										exes[2] = (int) (ProcessCard.this.fact
												* ProcessCard.this.xpl[0][ProcessCard.this.npt2 + i])
												+ ProcessCard.this.xt2;
										whys[2] = (int) (ProcessCard.this.fact
												* -ProcessCard.this.ypl[0][ProcessCard.this.npt2 + i])
												+ ProcessCard.this.yt2;
										camx[i] = (exes[1] + exes[2]) / 2;
										camy[i] = (whys[1] + whys[2]) / 2;
										Sys.this.processViewerImgBufGraphContext.fillPolygon(exes, whys, 4);
									}
									// p ut some info on the geometry
									if (ProcessCard.this.displ == 3) {
										Sys.this.processViewerImgBufGraphContext.setColor(Color.green);
										exes[1] = (int) (ProcessCard.this.fact * ProcessCard.this.xpl[0][1])
												+ ProcessCard.this.xt1 + 20;
										whys[1] = (int) (ProcessCard.this.fact * -ProcessCard.this.ypl[0][1])
												+ ProcessCard.this.yt1;
										exes[2] = (int) (ProcessCard.this.fact * ProcessCard.this.xpl[0][1])
												+ ProcessCard.this.xt2 + 20;
										whys[2] = (int) (ProcessCard.this.fact * -ProcessCard.this.ypl[0][1])
												+ ProcessCard.this.yt2;
										Sys.this.processViewerImgBufGraphContext.drawLine(exes[1], whys[1], exes[2],
												whys[2]);
										Sys.this.processViewerImgBufGraphContext.drawString("Span", exes[2] + 10,
												whys[2] + 10);

										exes[1] = (int) (ProcessCard.this.fact * ProcessCard.this.xpl[0][1])
												+ ProcessCard.this.xt2;
										whys[1] = (int) (ProcessCard.this.fact * -ProcessCard.this.ypl[0][1])
												+ ProcessCard.this.yt2 + 15;
										exes[2] = (int) (ProcessCard.this.fact
												* ProcessCard.this.xpl[0][ProcessCard.this.npt2])
												+ ProcessCard.this.xt2;
										whys[2] = whys[1];
										Sys.this.processViewerImgBufGraphContext.drawLine(exes[1], whys[1], exes[2],
												whys[2]);
										if (ProcessCard.this.foiltype <= FOILTYPE_PLATE) {
											Sys.this.processViewerImgBufGraphContext.drawString(Sys.CHORD2,
													exes[2] + 10, whys[2] + 15);
										}
										if (ProcessCard.this.foiltype >= FOILTYPE_CYLINDER) {
											Sys.this.processViewerImgBufGraphContext.drawString(Sys.DIAMETER,
													exes[2] + 10, whys[2] + 15);
										}

										Sys.this.processViewerImgBufGraphContext.drawString(Sys.FLOW, 40, 75);
										Sys.this.processViewerImgBufGraphContext.drawLine(30, 82, 60, 82);
										exes[0] = 60;
										exes[1] = 60;
										exes[2] = 70;
										whys[0] = 87;
										whys[1] = 77;
										whys[2] = 82;
										Sys.this.processViewerImgBufGraphContext.fillPolygon(exes, whys, 3);
									}
									// spin the cylinder and ball
									if (ProcessCard.this.foiltype >= FOILTYPE_CYLINDER) {
										exes[0] = (int) (ProcessCard.this.fact * (.5
												* (ProcessCard.this.xpl[0][1]
														+ ProcessCard.this.xpl[0][ProcessCard.this.npt2])
												+ ProcessCard.this.rval * Math.cos(
														ProcessCard.this.convdr * (ProcessCard.this.plthg[1] + 180.))))
												+ ProcessCard.this.xt2;
										whys[0] = (int) (ProcessCard.this.fact
												* (-ProcessCard.this.ypl[0][1] + ProcessCard.this.rval * Math.sin(
														ProcessCard.this.convdr * (ProcessCard.this.plthg[1] + 180.))))
												+ ProcessCard.this.yt2;
										exes[1] = (int) (ProcessCard.this.fact * (.5
												* (ProcessCard.this.xpl[0][1]
														+ ProcessCard.this.xpl[0][ProcessCard.this.npt2])
												+ ProcessCard.this.rval * Math
														.cos(ProcessCard.this.convdr * ProcessCard.this.plthg[1])))
												+ ProcessCard.this.xt2;
										whys[1] = (int) (ProcessCard.this.fact
												* (-ProcessCard.this.ypl[0][1] + ProcessCard.this.rval * Math
														.sin(ProcessCard.this.convdr * ProcessCard.this.plthg[1])))
												+ ProcessCard.this.yt2;
										Sys.this.processViewerImgBufGraphContext.setColor(Color.red);
										Sys.this.processViewerImgBufGraphContext.drawLine(exes[0], whys[0], exes[1],
												whys[1]);
									}
								}
							}

							Sys.this.processViewerImgBufGraphContext.setColor(Color.lightGray);
							Sys.this.processViewerImgBufGraphContext.fillRect(0, 295, 500, 50);
							Sys.this.processViewerImgBufGraphContext.setColor(Color.white);
							if (ProcessCard.this.viewflg == WALL_VIEW_TRANSPARENT) {
								Sys.this.processViewerImgBufGraphContext.setColor(Color.yellow);
							}
							Sys.this.processViewerImgBufGraphContext.fillRect(72, 302, 160, 20);
							Sys.this.processViewerImgBufGraphContext.setColor(Color.black);
							Sys.this.processViewerImgBufGraphContext.drawString("P-Transparent Surface", 80, 317);

							Sys.this.processViewerImgBufGraphContext.setColor(Color.white);
							if (ProcessCard.this.viewflg == WALL_VIEW_SOLID) {
								Sys.this.processViewerImgBufGraphContext.setColor(Color.yellow);
							}
							Sys.this.processViewerImgBufGraphContext.fillRect(240, 302, 150, 20);
							Sys.this.processViewerImgBufGraphContext.setColor(Color.black);
							Sys.this.processViewerImgBufGraphContext.drawString("Solid Surface", 268, 317);

							g.drawImage(Sys.this.processViewerImageBuffer, 0, 0, this);
						}
					}

					/**
					 * Probe location.
					 *
					 * @param exes
					 *            the exes
					 * @param whys
					 *            the whys
					 * @param radvec
					 *            the radvec
					 */
					private void probeLocation(final int[] exes, final int[] whys, double radvec) {
						int i;
						double slope;
						double xvec;
						double yvec;
						double yprs;
						double yprs1;
						if (ProcessCard.this.pboflag > 0 && ProcessCard.this.pypl <= 0.0) {
							Sys.this.processViewerImgBufGraphContext.setColor(Color.magenta);
							yprs = .1 * ProcessCard.this.pxpl;
							Sys.this.processViewerImgBufGraphContext.fillOval(
									(int) (ProcessCard.this.fact * ProcessCard.this.pxpl) + ProcessCard.this.xt,
									(int) (ProcessCard.this.fact * (-ProcessCard.this.pypl + yprs))
											+ ProcessCard.this.yt - 2,
									5, 5);
							Sys.this.processViewerImgBufGraphContext.setColor(Color.white);
							exes[0] = (int) (ProcessCard.this.fact * (ProcessCard.this.pxpl + .1))
									+ ProcessCard.this.xt;
							whys[0] = (int) (ProcessCard.this.fact * (-ProcessCard.this.pypl + yprs))
									+ ProcessCard.this.yt;
							exes[1] = (int) (ProcessCard.this.fact * (ProcessCard.this.pxpl + .5))
									+ ProcessCard.this.xt;
							whys[1] = (int) (ProcessCard.this.fact * (-ProcessCard.this.pypl + yprs))
									+ ProcessCard.this.yt;
							exes[2] = (int) (ProcessCard.this.fact * (ProcessCard.this.pxpl + .5))
									+ ProcessCard.this.xt;
							whys[2] = (int) (ProcessCard.this.fact * (-ProcessCard.this.pypl + 50.))
									+ ProcessCard.this.yt;
							Sys.this.processViewerImgBufGraphContext.drawLine(exes[0], whys[0], exes[1], whys[1]);
							Sys.this.processViewerImgBufGraphContext.drawLine(exes[1], whys[1], exes[2], whys[2]);
							if (ProcessCard.this.pboflag == PROBLE_SMOKE && ProcessCard.this.vfsd >= .01) { // smoke
																											// trail
								Sys.this.processViewerImgBufGraphContext.setColor(Color.green);
								for (i = 1; i <= ProcessCard.this.nptc - 1; ++i) {
									exes[0] = (int) (ProcessCard.this.fact * ProcessCard.this.xpl[19][i])
											+ ProcessCard.this.xt;
									yprs = .1 * ProcessCard.this.xpl[19][i];
									yprs1 = .1 * ProcessCard.this.xpl[19][i + 1];
									whys[0] = (int) (ProcessCard.this.fact * (-ProcessCard.this.ypl[19][i] + yprs))
											+ ProcessCard.this.yt;
									slope = (ProcessCard.this.ypl[19][i + 1] - yprs1 - ProcessCard.this.ypl[19][i]
											+ yprs) / (ProcessCard.this.xpl[19][i + 1] - ProcessCard.this.xpl[19][i]);
									xvec = ProcessCard.this.xpl[19][i] + radvec / Math.sqrt(1.0 + slope * slope);
									yvec = ProcessCard.this.ypl[19][i] - yprs
											+ slope * (xvec - ProcessCard.this.xpl[19][i]);
									exes[1] = (int) (ProcessCard.this.fact * xvec) + ProcessCard.this.xt;
									whys[1] = (int) (ProcessCard.this.fact * -yvec) + ProcessCard.this.yt;
									Sys.this.processViewerImgBufGraphContext.drawLine(exes[0], whys[0], exes[1],
											whys[1]);
									/*
									 * if ((i-antim)/3*3 == (i-antim) ) {
									 * off6Gg.drawLine(exes[0],whys[0],exes[1],whys[1]) ; }
									 */
								}
							}
						}
					}

					/**
					 * (non-Javadoc).
					 *
					 * @see java.lang.Runnable#run()
					 */
					@Override
					public void run() {
						int timer;

						timer = 100;
						while (true) {
							++ProcessCard.this.antim;
							try {
								Thread.sleep(timer);
							} catch (final InterruptedException e) {
							}
							ProcessOutput.this.view.repaint();
							if (ProcessCard.this.antim == 3) {
								ProcessCard.this.antim = 0;
								ProcessCard.this.ancol = -ProcessCard.this.ancol; /* MODS 27 JUL 99 */
							}
							timer = 135 - (int) (.227 * ProcessCard.this.vfsd / ProcessCard.this.vconv);
							// make the ball spin
							if (ProcessCard.this.foiltype >= FOILTYPE_CYLINDER) {
								ProcessCard.this.plthg[1] = ProcessCard.this.plthg[1]
										+ ProcessCard.this.spin * ProcessCard.this.spindr * 5.;
								if (ProcessCard.this.plthg[1] < -360.0) {
									ProcessCard.this.plthg[1] = ProcessCard.this.plthg[1] + 360.0;
								}
								if (ProcessCard.this.plthg[1] > 360.0) {
									ProcessCard.this.plthg[1] = ProcessCard.this.plthg[1] - 360.0;
								}
							}
						}
					}

					/** Start. */
					public void start() {
						if (this.runner == null) {
							this.runner = new Thread(this);
							this.runner.start();
						}
						ProcessCard.this.antim = 0; /* MODS 21 JUL 99 */
						ProcessCard.this.ancol = 1; /* MODS 27 JUL 99 */
						Sys.logger.info("Oup Process Viewer Started");
					}

					/**
					 * (non-Javadoc).
					 *
					 * @param g
					 *            the g
					 * @see java.awt.Canvas#update(java.awt.Graphics)
					 */
					@Override
					public void update(Graphics g) {
						ProcessOutput.this.view.paint(g);
					}
				} // end Viewer

				/** The Constant serialVersionUID. */
				private static final long serialVersionUID = 1L;

				/** The outerparent. */
				Sys outerparent;

				/** The plt 2. */
				ProcessPlotter2 plt2;

				/** The view. */
				ProcessViewer view;

				/**
				 * Instantiates a new oup.
				 *
				 * @param target
				 *            the target
				 */
				ProcessOutput(Sys target) {
					this.outerparent = target;
					ProcessCard.this.layout = new CardLayout();
					this.setLayout(ProcessCard.this.layout);

					this.plt2 = new ProcessPlotter2(this.outerparent);
					this.view = new ProcessViewer(this.outerparent);

					this.add(MainPanel.FIRST_CARD, this.plt2);
					this.add(MainPanel.SECOND_CARD, this.view);
				}
			} // end Oup

			/** The Class Plt. */
			class ProcessPlotter extends Canvas implements Runnable {

				/** The Constant serialVersionUID. */
				private static final long serialVersionUID = 1L;

				/** The ancp. */
				Point locp;
				// private Point ancp;

				/** The outerparent. */
				Sys outerparent;

				/** The run 2. */
				Thread run2;

				/**
				 * Instantiates a new plt.
				 *
				 * @param target
				 *            the target
				 */
				ProcessPlotter(Sys target) {
					this.setBackground(Color.green);
					this.run2 = null;
				}

				/**
				 * Gets the clplot.
				 *
				 * @param camb
				 *            the camb
				 * @param thic
				 *            the thic
				 * @param angl
				 *            the angl
				 * @return the clplot
				 */
				public double getClplot(double camb, double thic, double angl) {
					double beta, xc, yc, rc, gamc, lec, tec, lecm, tecm, crdc;
					double number;

					xc = 0.0;
					yc = camb / 2.0;
					rc = thic / 4.0 + Math.sqrt(thic * thic / 16.0 + yc * yc + 1.0);
					xc = 1.0 - Math.sqrt(rc * rc - yc * yc);
					beta = Math.asin(yc / rc) / ProcessCard.this.convdr; /* Kutta condition */
					gamc = 2.0 * rc * Math.sin((angl + beta) * ProcessCard.this.convdr);
					lec = xc - Math.sqrt(rc * rc - yc * yc);
					tec = xc + Math.sqrt(rc * rc - yc * yc);
					lecm = lec + 1.0 / lec;
					tecm = tec + 1.0 / tec;
					crdc = tecm - lecm;
					// stall model 1
					ProcessCard.this.stfact = 1.0;
					if (ProcessCard.this.anflag == 1) {
						if (angl > 10.0) {
							ProcessCard.this.stfact = .5 + .1 * angl - .005 * angl * angl;
						}
						if (angl < -10.0) {
							ProcessCard.this.stfact = .5 - .1 * angl - .005 * angl * angl;
						}
					}

					number = ProcessCard.this.stfact * gamc * 4.0 * Sys.PI / crdc;

					if (ProcessCard.this.arcor == 1) { // correction for low aspect ratio
						number = number / (1.0 + number / (3.14159 * ProcessCard.this.aspr));
					}

					return number;
				}

				/**
				 * Handleb.
				 *
				 * @param x
				 *            the x
				 * @param y
				 *            the y
				 */
				public void handleb(int x, int y) {
					if (y >= 2 && y <= 20) { // save plot
						if (x >= 350) {
							ProcessCard.this.plsav[ProcessCard.this.tstflag] = 1;
							ProcessCard.this.loadInput();
						}
						if (x >= 2 && x <= 80) { // rescale up
							ProcessCard.this.endy = 5.0 * ProcessCard.this.endy;
							ProcessCard.this.loadInput();
						}
						if (x >= 102 && x <= 180) { // rescale down
							ProcessCard.this.endy = ProcessCard.this.endy / 5.0;
							ProcessCard.this.loadInput();
						}
					}
					/*
					 * if (x >= 82 && x <= 232) { dispp = 0 ; calcrange = 0 ; computeFlow() ; } if
					 * (x >= 240 && x <= 390) { dispp = 1 ; calcrange = 0 ; computeFlow() ; }
					 */
					ProcessCard.this.plotter.repaint();
				}

				/** Load plot. */
				public void loadPlot() {
					double lftref, clref;
					double del, sped;
					double ppl, tpl, hpl, angl, thkpl, campl, clpl;
					int index, ic;

					ProcessCard.this.lines = 1;
					clref = this.getClplot(ProcessCard.this.camval, ProcessCard.this.thkval, ProcessCard.this.alfval);
					if (Math.abs(clref) <= .001) {
						clref = .001; /* protection */
					}
					lftref = clref * ProcessCard.this.q0 * ProcessCard.this.area / ProcessCard.this.lconv
							/ ProcessCard.this.lconv;
					// ******* attempt at constant re-scale
					/* endy = 10.0 ; begy = 0.0 ; calcrange = 0 ; */
					// ********

					// load up the view image
					for (ic = 0; ic <= ProcessCard.this.nlnc; ++ic) {
						for (index = 0; index <= ProcessCard.this.nptc; ++index) {
							if (ProcessCard.this.foiltype <= FOILTYPE_PLATE) {
								ProcessCard.this.xpl[ic][index] = ProcessCard.this.xm[ic][index];
								ProcessCard.this.ypl[ic][index] = ProcessCard.this.ym[ic][index];
							}
							if (ProcessCard.this.foiltype >= FOILTYPE_CYLINDER) {
								ProcessCard.this.xpl[ic][index] = ProcessCard.this.xg[ic][index];
								ProcessCard.this.ypl[ic][index] = ProcessCard.this.yg[ic][index];
							}
						}
					}

					if (ProcessCard.this.dispp == 2) { // lift versus angle
						ProcessCard.this.npt = 20;
						ProcessCard.this.ntr = 1;
						ProcessCard.this.nabs = 2;
						ProcessCard.this.nord = 3;
						ProcessCard.this.begx = -20.0;
						ProcessCard.this.endx = 20.0;
						ProcessCard.this.ntikx = 5;
						ProcessCard.this.labx = String.valueOf("Angle ");
						ProcessCard.this.labxu = String.valueOf("degrees");
						del = 40.0 / ProcessCard.this.npt;
						for (ic = 1; ic <= ProcessCard.this.npt; ++ic) {
							angl = -20.0 + (ic - 1) * del;
							clpl = this.getClplot(ProcessCard.this.camval, ProcessCard.this.thkval, angl);
							ProcessCard.this.pltx[0][ic] = angl;
							if (ProcessCard.this.dout == 0) {
								ProcessCard.this.plty[0][ic] = ProcessCard.this.fconv * lftref * clpl / clref;
							}
							if (ProcessCard.this.dout == 1) {
								ProcessCard.this.plty[0][ic] = 100. * clpl;
							}
						}
						ProcessCard.this.ntiky = 6;
						ProcessCard.this.pltx[1][0] = ProcessCard.this.alfval;
						if (ProcessCard.this.dout == 0) {
							ProcessCard.this.laby = Sys.LIFT2;
							if (ProcessCard.this.lunits == UNITS_ENGLISH) {
								ProcessCard.this.labyu = Sys.LBS;
							}
							if (ProcessCard.this.lunits == UNITS_METERIC) {
								ProcessCard.this.labyu = "N";
							}
							ProcessCard.this.plty[1][0] = lftref * ProcessCard.this.fconv;
						}
						if (ProcessCard.this.dout == 1) {
							ProcessCard.this.laby = "Cl";
							ProcessCard.this.labyu = Sys.X_100;
							ProcessCard.this.plty[1][0] = 100. * ProcessCard.this.clift;
						}
						// test data
						for (ic = 1; ic <= ProcessCard.this.numpt[ProcessCard.this.tstflag]; ++ic) {
							ProcessCard.this.plttx[1][ic] = ProcessCard.this.ang[ProcessCard.this.tstflag][ic];
							clpl = this.getClplot(ProcessCard.this.camval, ProcessCard.this.thkval,
									ProcessCard.this.plttx[1][ic]);
							if (ProcessCard.this.dout == 0) {
								ProcessCard.this.pltty[1][ic] = ProcessCard.this.fconv
										* ProcessCard.this.lft[ProcessCard.this.tstflag][ic];
							}
							// if (dout == 1)pltty[1][ic] = 100. * clpl ;
							if (ProcessCard.this.dout == 1) {
								ProcessCard.this.pltty[1][ic] = 100. * ProcessCard.this.fconv
										* ProcessCard.this.lft[ProcessCard.this.tstflag][ic]
										/ (ProcessCard.this.area * ProcessCard.this.qdt[ProcessCard.this.tstflag][ic]
												* ProcessCard.this.ppconv);
							}
							ProcessCard.this.plt2x[ProcessCard.this.tstflag][ic] = ProcessCard.this.plttx[1][ic];
							ProcessCard.this.plt2y[ProcessCard.this.tstflag][ic] = ProcessCard.this.pltty[1][ic];
						}
						ProcessCard.this.reorder();
						// inp.flt.lwr.d1.setText(String.valueOf(filter3(fconv*lft[tstflag][5]))) ;
						// inp.flt.lwr.d2.setText(String.valueOf(filter3(plt2y[tstflag][5]))) ;
						// inp.flt.lwr.d3.setText(String.valueOf(filter3(qdt[tstflag][5]*ppconv))) ;
					}
					if (ProcessCard.this.dispp == 3) { // lift versus thickness
						ProcessCard.this.npt = 20;
						ProcessCard.this.ntr = 1;
						ProcessCard.this.nabs = 3;
						ProcessCard.this.nord = 3;
						ProcessCard.this.begx = 0.0;
						ProcessCard.this.endx = 25.0;
						ProcessCard.this.ntikx = 6;
						ProcessCard.this.labx = Sys.THICKNESS_SPACE;
						ProcessCard.this.labxu = Sys.CHORD3;
						del = 1.0 / ProcessCard.this.npt;
						for (ic = 1; ic <= ProcessCard.this.npt; ++ic) {
							thkpl = .05 + (ic - 1) * del;
							clpl = this.getClplot(ProcessCard.this.camval, thkpl, ProcessCard.this.alfval);
							ProcessCard.this.pltx[0][ic] = thkpl * 25.;
							if (ProcessCard.this.dout == 0) {
								ProcessCard.this.plty[0][ic] = ProcessCard.this.fconv * lftref * clpl / clref;
							}
							if (ProcessCard.this.dout == 1) {
								ProcessCard.this.plty[0][ic] = 100. * clpl;
							}
						}
						ProcessCard.this.ntiky = 5;
						ProcessCard.this.pltx[1][0] = ProcessCard.this.thkinpt;
						if (ProcessCard.this.dout == 0) {
							ProcessCard.this.laby = Sys.LIFT2;
							if (ProcessCard.this.lunits == UNITS_ENGLISH) {
								ProcessCard.this.labyu = Sys.LBS;
							}
							if (ProcessCard.this.lunits == UNITS_METERIC) {
								ProcessCard.this.labyu = "N";
							}
							ProcessCard.this.plty[1][0] = lftref * ProcessCard.this.fconv;
						}
						if (ProcessCard.this.dout == 1) {
							ProcessCard.this.laby = "Cl";
							ProcessCard.this.labyu = Sys.X_100;
							ProcessCard.this.plty[1][0] = 100. * ProcessCard.this.clift;
						}
					}
					if (ProcessCard.this.dispp == 4) { // lift versus camber
						ProcessCard.this.npt = 20;
						ProcessCard.this.ntr = 1;
						ProcessCard.this.nabs = 4;
						ProcessCard.this.nord = 3;
						ProcessCard.this.begx = -25.;
						ProcessCard.this.endx = 25.;
						ProcessCard.this.ntikx = 5;
						ProcessCard.this.labx = "Camber ";
						ProcessCard.this.labxu = Sys.CHORD3;
						del = 2.0 / ProcessCard.this.npt;
						for (ic = 1; ic <= ProcessCard.this.npt; ++ic) {
							campl = -1.0 + (ic - 1) * del;
							clpl = this.getClplot(campl, ProcessCard.this.thkval, ProcessCard.this.alfval);
							ProcessCard.this.pltx[0][ic] = campl * 25.0;
							if (ProcessCard.this.dout == 0) {
								ProcessCard.this.plty[0][ic] = ProcessCard.this.fconv * lftref * clpl / clref;
							}
							if (ProcessCard.this.dout == 1) {
								ProcessCard.this.plty[0][ic] = 100. * clpl;
							}
						}
						ProcessCard.this.ntiky = 5;
						ProcessCard.this.pltx[1][0] = ProcessCard.this.caminpt;
						if (ProcessCard.this.dout == 0) {
							ProcessCard.this.laby = Sys.LIFT2;
							if (ProcessCard.this.lunits == UNITS_ENGLISH) {
								ProcessCard.this.labyu = Sys.LBS;
							}
							if (ProcessCard.this.lunits == UNITS_METERIC) {
								ProcessCard.this.labyu = "N";
							}
							ProcessCard.this.plty[1][0] = lftref * ProcessCard.this.fconv;
						}
						if (ProcessCard.this.dout == 1) {
							ProcessCard.this.laby = "Cl";
							ProcessCard.this.labyu = Sys.X_100;
							ProcessCard.this.plty[1][0] = 100. * ProcessCard.this.clift;
						}
					}
					if (ProcessCard.this.dispp == 5) { // lift versus speed
						ProcessCard.this.npt = 20;
						ProcessCard.this.ntr = 1;
						ProcessCard.this.nabs = 5;
						ProcessCard.this.nord = 3;
						ProcessCard.this.begx = 0.0;
						ProcessCard.this.endx = 300.0;
						ProcessCard.this.ntikx = 7;
						ProcessCard.this.labx = "Speed ";
						if (ProcessCard.this.lunits == UNITS_ENGLISH) {
							ProcessCard.this.labxu = Sys.MPH;
						}
						if (ProcessCard.this.lunits == UNITS_METERIC) {
							ProcessCard.this.labxu = "kmh";
						}
						del = ProcessCard.this.vmax / ProcessCard.this.npt;
						for (ic = 1; ic <= ProcessCard.this.npt; ++ic) {
							sped = (ic - 1) * del;
							ProcessCard.this.pltx[0][ic] = sped;
							if (ProcessCard.this.dout == 0) {
								ProcessCard.this.plty[0][ic] = ProcessCard.this.fconv * lftref * sped * sped
										/ (ProcessCard.this.vfsd * ProcessCard.this.vfsd);
							}
							if (ProcessCard.this.dout == 1) {
								ProcessCard.this.plty[0][ic] = 100. * ProcessCard.this.clift;
							}
						}
						ProcessCard.this.ntiky = 6;
						ProcessCard.this.laby = Sys.LIFT2;
						ProcessCard.this.pltx[1][0] = ProcessCard.this.vfsd;
						if (ProcessCard.this.dout == 0) {
							ProcessCard.this.laby = Sys.LIFT2;
							if (ProcessCard.this.lunits == UNITS_ENGLISH) {
								ProcessCard.this.labyu = Sys.LBS;
							}
							if (ProcessCard.this.lunits == UNITS_METERIC) {
								ProcessCard.this.labyu = "N";
							}
							ProcessCard.this.plty[1][0] = lftref * ProcessCard.this.fconv;
						}
						if (ProcessCard.this.dout == 1) {
							ProcessCard.this.laby = "Cl";
							ProcessCard.this.labyu = Sys.X_100;
							ProcessCard.this.plty[1][0] = 100. * ProcessCard.this.clift;
						}
						// test data
						for (ic = 1; ic <= ProcessCard.this.numpt[ProcessCard.this.tstflag]; ++ic) {
							ProcessCard.this.plttx[1][ic] = ProcessCard.this.vcon2
									* ProcessCard.this.spd[ProcessCard.this.tstflag][ic];
							if (ProcessCard.this.dout == 0) {
								ProcessCard.this.pltty[1][ic] = ProcessCard.this.fconv
										* ProcessCard.this.lft[ProcessCard.this.tstflag][ic];
							}
							if (ProcessCard.this.dout == 1) {
								ProcessCard.this.pltty[1][ic] = 100. * ProcessCard.this.fconv
										* ProcessCard.this.lft[ProcessCard.this.tstflag][ic]
										/ (ProcessCard.this.area * ProcessCard.this.qdt[ProcessCard.this.tstflag][ic]
												* ProcessCard.this.ppconv);
							}
							ProcessCard.this.plt2x[ProcessCard.this.tstflag][ic] = ProcessCard.this.plttx[1][ic];
							ProcessCard.this.plt2y[ProcessCard.this.tstflag][ic] = ProcessCard.this.pltty[1][ic];
						}
						ProcessCard.this.reorder();
					}
					if (ProcessCard.this.dispp == 6) { // lift versus altitude
						ProcessCard.this.npt = 20;
						ProcessCard.this.ntr = 1;
						ProcessCard.this.nabs = 6;
						ProcessCard.this.nord = 3;
						ProcessCard.this.begx = 0.0;
						ProcessCard.this.endx = 50.0;
						ProcessCard.this.ntikx = 6;
						if (ProcessCard.this.lunits == UNITS_ENGLISH) {
							ProcessCard.this.endx = 50.0;
						}
						if (ProcessCard.this.lunits == UNITS_METERIC) {
							ProcessCard.this.endx = 15.0;
						}
						ProcessCard.this.labx = Sys.ALTITUDE;
						if (ProcessCard.this.lunits == UNITS_ENGLISH) {
							ProcessCard.this.labxu = "k-ft";
						}
						if (ProcessCard.this.lunits == UNITS_METERIC) {
							ProcessCard.this.labxu = "km";
						}
						del = ProcessCard.this.altmax / ProcessCard.this.npt;
						for (ic = 1; ic <= ProcessCard.this.npt; ++ic) {
							hpl = (ic - 1) * del;
							ProcessCard.this.pltx[0][ic] = ProcessCard.this.lconv * hpl / 1000.;
							tpl = 518.6;
							ppl = 2116.217;
							if (ProcessCard.this.planet == 0) {
								if (hpl < 36152.) {
									tpl = 518.6 - 3.56 * hpl / 1000.;
									ppl = 2116.217 * Math.pow(tpl / 518.6, 5.256);
								} else {
									tpl = 389.98;
									ppl = 2116.217 * .236 * Math.exp((36000. - hpl) / (53.35 * tpl));
								}
								ProcessCard.this.plty[0][ic] = ProcessCard.this.fconv * lftref * ppl
										/ (tpl * 53.3 * 32.17) / ProcessCard.this.rho;
							}
							if (ProcessCard.this.planet == 1) {
								if (hpl <= 22960.) {
									tpl = 434.02 - .548 * hpl / 1000.;
									ppl = 14.62 * Math.pow(2.71828, -.00003 * hpl);
								}
								if (hpl > 22960.) {
									tpl = 449.36 - 1.217 * hpl / 1000.;
									ppl = 14.62 * Math.pow(2.71828, -.00003 * hpl);
								}
								ProcessCard.this.plty[0][ic] = ProcessCard.this.fconv * lftref * ppl / (tpl * 1149.)
										/ ProcessCard.this.rho;
							}
							if (ProcessCard.this.planet == 2) {
								ProcessCard.this.plty[0][ic] = ProcessCard.this.fconv * lftref;
							}
						}
						ProcessCard.this.ntiky = 5;
						ProcessCard.this.laby = Sys.LIFT2;
						ProcessCard.this.pltx[1][0] = ProcessCard.this.alt / 1000.;
						ProcessCard.this.plty[1][0] = lftref * ProcessCard.this.fconv;
						if (ProcessCard.this.lunits == UNITS_ENGLISH) {
							ProcessCard.this.labyu = Sys.LBS;
						}
						if (ProcessCard.this.lunits == UNITS_METERIC) {
							ProcessCard.this.labyu = "N";
						}
					}
					if (ProcessCard.this.dispp == 7) { // lift versus area
						ProcessCard.this.npt = 2;
						ProcessCard.this.ntr = 1;
						ProcessCard.this.nabs = 7;
						ProcessCard.this.nord = 3;
						ProcessCard.this.begx = 0.0;
						ProcessCard.this.ntikx = 6;
						ProcessCard.this.labx = "Area ";
						if (ProcessCard.this.lunits == UNITS_ENGLISH) {
							ProcessCard.this.labxu = "sq ft";
							ProcessCard.this.endx = 2000.0;
							ProcessCard.this.labyu = Sys.LBS;
							ProcessCard.this.pltx[0][1] = 0.0;
							ProcessCard.this.plty[0][1] = 0.0;
							ProcessCard.this.pltx[0][2] = 2000.;
							ProcessCard.this.plty[0][2] = ProcessCard.this.fconv * lftref * 2000.
									/ ProcessCard.this.area;
						}
						if (ProcessCard.this.lunits == UNITS_METERIC) {
							ProcessCard.this.labxu = "sq m";
							ProcessCard.this.endx = 200.;
							ProcessCard.this.labyu = "N";
							ProcessCard.this.pltx[0][1] = 0.0;
							ProcessCard.this.plty[0][1] = 0.0;
							ProcessCard.this.pltx[0][2] = 200.;
							ProcessCard.this.plty[0][2] = ProcessCard.this.fconv * lftref * 200.
									/ ProcessCard.this.area;
							;
						}

						ProcessCard.this.ntiky = 5;
						ProcessCard.this.laby = Sys.LIFT2;
						ProcessCard.this.pltx[1][0] = ProcessCard.this.area;
						ProcessCard.this.plty[1][0] = lftref * ProcessCard.this.fconv;
					}
					if (ProcessCard.this.dispp == 8) { // lift versus density
						ProcessCard.this.npt = 2;
						ProcessCard.this.ntr = 1;
						ProcessCard.this.nabs = 7;
						ProcessCard.this.nord = 3;
						ProcessCard.this.begx = 0.0;
						ProcessCard.this.ntikx = 6;
						ProcessCard.this.labx = "Density ";
						if (ProcessCard.this.planet == 0) {
							if (ProcessCard.this.lunits == UNITS_ENGLISH) {
								ProcessCard.this.labxu = "x 10,000 slug/cu ft";
								ProcessCard.this.endx = 25.0;
								ProcessCard.this.pltx[0][1] = 0.0;
								ProcessCard.this.plty[0][1] = 0.0;
								ProcessCard.this.pltx[0][2] = 23.7;
								ProcessCard.this.plty[0][2] = ProcessCard.this.fconv * lftref * 23.7
										/ (ProcessCard.this.rho * 10000.);
								ProcessCard.this.pltx[1][0] = ProcessCard.this.rho * 10000.;
							}
							if (ProcessCard.this.lunits == UNITS_METERIC) {
								ProcessCard.this.labxu = Sys.G_CU_M;
								ProcessCard.this.endx = 1250.;
								ProcessCard.this.pltx[0][1] = 0.0;
								ProcessCard.this.plty[0][1] = 0.0;
								ProcessCard.this.pltx[0][2] = 1226;
								ProcessCard.this.plty[0][2] = ProcessCard.this.fconv * lftref * 23.7
										/ (ProcessCard.this.rho * 10000.);
								ProcessCard.this.pltx[1][0] = ProcessCard.this.rho * 1000. * 515.4;
							}
						}
						if (ProcessCard.this.planet == 1) {
							if (ProcessCard.this.lunits == UNITS_ENGLISH) {
								ProcessCard.this.labxu = "x 100,000 slug/cu ft";
								ProcessCard.this.endx = 5.0;
								ProcessCard.this.pltx[0][1] = 0.0;
								ProcessCard.this.plty[0][1] = 0.0;
								ProcessCard.this.pltx[0][2] = 2.93;
								ProcessCard.this.plty[0][2] = ProcessCard.this.fconv * lftref * 2.93
										/ (ProcessCard.this.rho * 100000.);
								ProcessCard.this.pltx[1][0] = ProcessCard.this.rho * 100000.;
							}
							if (ProcessCard.this.lunits == UNITS_METERIC) {
								ProcessCard.this.labxu = Sys.G_CU_M;
								ProcessCard.this.endx = 15.;
								ProcessCard.this.pltx[0][1] = 0.0;
								ProcessCard.this.plty[0][1] = 0.0;
								ProcessCard.this.pltx[0][2] = 15.1;
								ProcessCard.this.plty[0][2] = ProcessCard.this.fconv * lftref * 2.93
										/ (ProcessCard.this.rho * 100000.);
								ProcessCard.this.pltx[1][0] = ProcessCard.this.rho * 1000. * 515.4;
							}
						}
						ProcessCard.this.ntiky = 5;
						ProcessCard.this.laby = Sys.LIFT2;
						ProcessCard.this.plty[1][0] = lftref * ProcessCard.this.fconv;
						if (ProcessCard.this.lunits == UNITS_ENGLISH) {
							ProcessCard.this.labyu = Sys.LBS;
						}
						if (ProcessCard.this.lunits == UNITS_METERIC) {
							ProcessCard.this.labyu = "N";
						}
					}
					if (ProcessCard.this.dispp == 9) { // lift versus pressure
						ProcessCard.this.npt = 2;
						ProcessCard.this.ntr = 1;
						ProcessCard.this.nabs = 7;
						ProcessCard.this.nord = 3;
						ProcessCard.this.ntikx = 6;
						ProcessCard.this.labx = "Pressure ";
						if (ProcessCard.this.lunits == UNITS_ENGLISH) {
							ProcessCard.this.labxu = "lb/sq ft";
							ProcessCard.this.begx = 100.;
							ProcessCard.this.endx = 2500.0;
							ProcessCard.this.pltx[0][1] = ProcessCard.this.begx;
							if (ProcessCard.this.dout == 0) {
								ProcessCard.this.plty[0][1] = ProcessCard.this.fconv * lftref * ProcessCard.this.begx
										/ ProcessCard.this.ps0;
							}
							if (ProcessCard.this.dout == 1) {
								ProcessCard.this.plty[0][1] = 100. * ProcessCard.this.clift;
							}
							ProcessCard.this.pltx[0][2] = ProcessCard.this.endx;
							if (ProcessCard.this.dout == 0) {
								ProcessCard.this.plty[0][2] = ProcessCard.this.fconv * lftref * ProcessCard.this.endx
										/ ProcessCard.this.ps0;
							}
							if (ProcessCard.this.dout == 1) {
								ProcessCard.this.plty[0][2] = 100. * ProcessCard.this.clift;
							}
							ProcessCard.this.pltx[1][0] = ProcessCard.this.psin;
						}
						if (ProcessCard.this.lunits == UNITS_METERIC) {
							ProcessCard.this.labxu = "kPa";
							ProcessCard.this.begx = 100 * ProcessCard.this.piconv;
							ProcessCard.this.endx = 2500. * ProcessCard.this.piconv;
							ProcessCard.this.pltx[0][1] = ProcessCard.this.begx;
							if (ProcessCard.this.dout == 0) {
								ProcessCard.this.plty[0][1] = ProcessCard.this.fconv * lftref * ProcessCard.this.begx
										/ (ProcessCard.this.ps0 * ProcessCard.this.piconv);
							}
							if (ProcessCard.this.dout == 1) {
								ProcessCard.this.plty[0][1] = 100. * ProcessCard.this.clift;
							}
							ProcessCard.this.pltx[0][2] = ProcessCard.this.endx;
							if (ProcessCard.this.dout == 0) {
								ProcessCard.this.plty[0][2] = ProcessCard.this.fconv * lftref * ProcessCard.this.endx
										/ (ProcessCard.this.ps0 * ProcessCard.this.piconv);
							}
							if (ProcessCard.this.dout == 1) {
								ProcessCard.this.plty[0][2] = 100. * ProcessCard.this.clift;
							}
							ProcessCard.this.pltx[1][0] = ProcessCard.this.ps0 * ProcessCard.this.piconv;
						}
						ProcessCard.this.ntiky = 6;
						if (ProcessCard.this.dout == 0) {
							ProcessCard.this.laby = Sys.LIFT2;
							if (ProcessCard.this.lunits == UNITS_ENGLISH) {
								ProcessCard.this.labyu = Sys.LBS;
							}
							if (ProcessCard.this.lunits == UNITS_METERIC) {
								ProcessCard.this.labyu = "N";
							}
							ProcessCard.this.plty[1][0] = lftref * ProcessCard.this.fconv;
						}
						if (ProcessCard.this.dout == 1) {
							ProcessCard.this.laby = "Cl";
							ProcessCard.this.labyu = Sys.X_100;
							ProcessCard.this.plty[1][0] = 100. * ProcessCard.this.clift;
						}
						// test data
						for (ic = 1; ic <= ProcessCard.this.numpt[ProcessCard.this.tstflag]; ++ic) {
							ProcessCard.this.plttx[1][ic] = ProcessCard.this.piconv
									* ProcessCard.this.pin[ProcessCard.this.tstflag][ic];
							if (ProcessCard.this.dout == 0) {
								ProcessCard.this.pltty[1][ic] = ProcessCard.this.fconv
										* ProcessCard.this.lft[ProcessCard.this.tstflag][ic];
							}
							if (ProcessCard.this.dout == 1) {
								ProcessCard.this.pltty[1][ic] = 100. * ProcessCard.this.fconv
										* ProcessCard.this.lft[ProcessCard.this.tstflag][ic]
										/ (ProcessCard.this.area * ProcessCard.this.qdt[ProcessCard.this.tstflag][ic]
												* ProcessCard.this.ppconv);
							}
							ProcessCard.this.plt2x[ProcessCard.this.tstflag][ic] = ProcessCard.this.plttx[1][ic];
							ProcessCard.this.plt2y[ProcessCard.this.tstflag][ic] = ProcessCard.this.pltty[1][ic];
						}
						ProcessCard.this.reorder();
					}
					// determine y range - zero in the middle
					/*
					 * if (dispp>= 2 && dispp < 6) { if (plty[0][npt] >= plty[0][1]) { begy=0.0 ; if
					 * (plty[0][1] > endy) endy = plty[0][1] ; if (plty[0][npt] > endy) endy =
					 * plty[0][npt] ; if (endy <= 0.0) { begy = plty[0][1] ; endy = plty[0][npt] ; }
					 * } if (plty[0][npt] < plty[0][1]) { endy=0.0 ; if (plty[0][1] < begy) begy =
					 * plty[0][1] ; if (plty[0][npt] < begy) begy = plty[0][npt] ; if (begy <= 0.0)
					 * { begy = plty[0][npt] ; endy = plty[0][1] ; } } }
					 */
					// determine y range
					/*
					 * if (dispp >= 6 && dispp <= 8) { if (plty[0][npt] >= plty[0][1]) { begy =
					 * plty[0][1] ; endy = plty[0][npt] ; } if (plty[0][npt] < plty[0][1]) { begy =
					 * plty[0][npt] ; endy = plty[0][1] ; } }
					 */
					// determine y range
					/*
					 * if (dispp >= 0 && dispp <= 1) { if (calcrange == 0) { begy = plty[0][1] ;
					 * endy = plty[0][1] ; for (index = 1; index <= npt2; ++ index) { if
					 * (plty[0][index] < begy) begy = plty[0][index] ; if (plty[1][index] < begy)
					 * begy = plty[1][index] ; if (plty[0][index] > endy) endy = plty[0][index] ; if
					 * (plty[1][index] > endy) endy = plty[1][index] ; } calcrange = 1 ; } }
					 * inp.flt.lwr.d1.setText(String.valueOf(dispp)) ;
					 */
				}

				/**
				 * (non-Javadoc).
				 *
				 * @param evt
				 *            the evt
				 * @param x
				 *            the x
				 * @param y
				 *            the y
				 * @return true, if successful
				 * @see java.awt.Component#mouseUp(java.awt.Event, int, int)
				 */
				@Override
				public boolean mouseUp(Event evt, int x, int y) {
					this.handleb(x, y);
					return true;
				}

				/**
				 * (non-Javadoc).
				 *
				 * @param g
				 *            the g
				 * @see java.awt.Canvas#paint(java.awt.Graphics)
				 */
				@Override
				public void paint(Graphics g) {
					final boolean b = true;
					if (b && Sys.this.mainPannel.processCard.isVisible()) {
						int i, j;
						int xlabel, ylabel, ind;
						final int exes[] = new int[8];
						final int whys[] = new int[8];
						double offx;
						double scalex;
						double offy;
						double scaley;
						double incy;
						double incx;
						double xl;
						double yl;
						int pltdata;
						pltdata = 0;
						if (ProcessCard.this.dispp <= 1) {
							Sys.this.processPlatterImgBuffGraphContext.setColor(Color.black);
							Sys.this.processPlatterImgBuffGraphContext.fillRect(0, 0, Sys.PROCESS_PLOTER_WIDTH,
									Sys.PROCESS_PLOTTER_HEIGHT);
							/*
							 * off7Gg.setColor(Color.white) ; off7Gg.fillRect(2,302,70,15) ;
							 * off7Gg.setColor(Color.red) ; off7Gg.drawString("Rescale",8,315) ;
							 */
							Sys.this.processPlatterImgBuffGraphContext.setColor(Color.lightGray);
							Sys.this.processPlatterImgBuffGraphContext.fillRect(0, 295, 500, 50);
							Sys.this.processPlatterImgBuffGraphContext.setColor(Color.white);
							if (ProcessCard.this.dispp == 0) {
								Sys.this.processPlatterImgBuffGraphContext.setColor(Color.yellow);
							}
							Sys.this.processPlatterImgBuffGraphContext.fillRect(82, 302, 150, 20);
							Sys.this.processPlatterImgBuffGraphContext.setColor(Color.black);
							Sys.this.processPlatterImgBuffGraphContext.drawString("Surface Pressure", 88, 317);

							Sys.this.processPlatterImgBuffGraphContext.setColor(Color.white);
							if (ProcessCard.this.dispp == 1) {
								Sys.this.processPlatterImgBuffGraphContext.setColor(Color.yellow);
							}
							Sys.this.processPlatterImgBuffGraphContext.fillRect(240, 302, 150, 20);
							Sys.this.processPlatterImgBuffGraphContext.setColor(Color.black);
							Sys.this.processPlatterImgBuffGraphContext.drawString(Sys.VELOCITY, 288, 317);
						}
						if (ProcessCard.this.dispp > 1 && ProcessCard.this.dispp <= 15) {
							Sys.this.processPlatterImgBuffGraphContext.setColor(Color.blue);
							Sys.this.processPlatterImgBuffGraphContext.fillRect(0, 0, 500, 500);

							Sys.this.processPlatterImgBuffGraphContext.setColor(Color.white);
							Sys.this.processPlatterImgBuffGraphContext.fillRect(2, 2, 70, 17);
							Sys.this.processPlatterImgBuffGraphContext.setColor(Color.red);
							Sys.this.processPlatterImgBuffGraphContext.drawString("+ Rescale", 8, 15);

							Sys.this.processPlatterImgBuffGraphContext.setColor(Color.white);
							Sys.this.processPlatterImgBuffGraphContext.fillRect(102, 2, 70, 17);
							Sys.this.processPlatterImgBuffGraphContext.setColor(Color.red);
							Sys.this.processPlatterImgBuffGraphContext.drawString("- Rescale", 108, 15);
						}
						if (ProcessCard.this.ntikx < 2) {
							ProcessCard.this.ntikx = 2; /* protection 13June96 */
						}
						if (ProcessCard.this.ntiky < 2) {
							ProcessCard.this.ntiky = 2;
						}
						offx = 0.0 - ProcessCard.this.begx;
						scalex = 6.0 / (ProcessCard.this.endx - ProcessCard.this.begx);
						incx = (ProcessCard.this.endx - ProcessCard.this.begx) / (ProcessCard.this.ntikx - 1);
						offy = 0.0 - ProcessCard.this.begy;
						scaley = 4.5 / (ProcessCard.this.endy - ProcessCard.this.begy);
						incy = (ProcessCard.this.endy - ProcessCard.this.begy) / (ProcessCard.this.ntiky - 1);

						if (ProcessCard.this.dispp <= 15) { /* draw a graph */
							/* draw axes */
							Sys.this.processPlatterImgBuffGraphContext.setColor(Color.white);
							exes[0] = (int) (ProcessCard.this.factp * 0.0) + ProcessCard.this.xtp;
							whys[0] = (int) (ProcessCard.this.factp * -4.5) + ProcessCard.this.ytp;
							exes[1] = (int) (ProcessCard.this.factp * 0.0) + ProcessCard.this.xtp;
							whys[1] = (int) (ProcessCard.this.factp * 0.0) + ProcessCard.this.ytp;
							exes[2] = (int) (ProcessCard.this.factp * 6.0) + ProcessCard.this.xtp;
							whys[2] = (int) (ProcessCard.this.factp * 0.0) + ProcessCard.this.ytp;
							Sys.this.processPlatterImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1], whys[1]);
							Sys.this.processPlatterImgBuffGraphContext.drawLine(exes[1], whys[1], exes[2], whys[2]);

							xlabel = (int) -90.0 + ProcessCard.this.xtp; /* label y axis */
							ylabel = (int) (ProcessCard.this.factp * -1.5) + ProcessCard.this.ytp;
							Sys.this.processPlatterImgBuffGraphContext.drawString(ProcessCard.this.laby, xlabel,
									ylabel);
							Sys.this.processPlatterImgBuffGraphContext.drawString(ProcessCard.this.labyu, xlabel,
									ylabel + 10);
							/* add tick values */
							for (ind = 1; ind <= ProcessCard.this.ntiky; ++ind) {
								xlabel = (int) -50.0 + ProcessCard.this.xtp;
								yl = ProcessCard.this.begy + (ind - 1) * incy;
								ylabel = (int) (ProcessCard.this.factp * -scaley * (yl + offy)) + ProcessCard.this.ytp;
								if (ProcessCard.this.nord >= 2) {
									Sys.this.processPlatterImgBuffGraphContext.drawString(String.valueOf((int) yl),
											xlabel, ylabel);
								} else {
									Sys.this.processPlatterImgBuffGraphContext
											.drawString(String.valueOf(Sys.filter3(yl)), xlabel, ylabel);
								}
							}
							xlabel = (int) (ProcessCard.this.factp * 3.0) + ProcessCard.this.xtp; /* label x axis */
							ylabel = (int) 40.0 + ProcessCard.this.ytp;
							Sys.this.processPlatterImgBuffGraphContext.drawString(ProcessCard.this.labx, xlabel,
									ylabel - 10);
							Sys.this.processPlatterImgBuffGraphContext.drawString(ProcessCard.this.labxu, xlabel,
									ylabel);
							/* add tick values */
							for (ind = 1; ind <= ProcessCard.this.ntikx; ++ind) {
								ylabel = (int) 15. + ProcessCard.this.ytp;
								xl = ProcessCard.this.begx + (ind - 1) * incx;
								xlabel = (int) (ProcessCard.this.factp * (scalex * (xl + offx) - .05))
										+ ProcessCard.this.xtp;
								if (ProcessCard.this.nabs == 1) {
									Sys.this.processPlatterImgBuffGraphContext.drawString(String.valueOf(xl), xlabel,
											ylabel);
								}
								if (ProcessCard.this.nabs > 1) {
									Sys.this.processPlatterImgBuffGraphContext.drawString(String.valueOf((int) xl),
											xlabel, ylabel);
								}
							}
							// draw plot
							// test data
							if (ProcessCard.this.dispp == 9 && ProcessCard.this.testp[ProcessCard.this.tstflag] == 3) {
								pltdata = 1;
							}
							if (ProcessCard.this.dispp == 2 && ProcessCard.this.testp[ProcessCard.this.tstflag] == 2) {
								pltdata = 1;
							}
							if (ProcessCard.this.dispp == 5 && ProcessCard.this.testp[ProcessCard.this.tstflag] == 1) {
								pltdata = 1;
							}

							if (pltdata == 1) {
								// new data
								Sys.this.processPlatterImgBuffGraphContext.setColor(Color.white);
								exes[1] = (int) (ProcessCard.this.factp * scalex
										* (offx + ProcessCard.this.plt2x[ProcessCard.this.tstflag][1]))
										+ ProcessCard.this.xtp;
								whys[1] = (int) (ProcessCard.this.factp * -scaley
										* (offy + ProcessCard.this.plt2y[ProcessCard.this.tstflag][1]))
										+ ProcessCard.this.ytp;
								for (i = 2; i <= ProcessCard.this.numpt[ProcessCard.this.tstflag]; ++i) {
									exes[0] = exes[1];
									whys[0] = whys[1];
									exes[1] = (int) (ProcessCard.this.factp * scalex
											* (offx + ProcessCard.this.plt2x[ProcessCard.this.tstflag][i]))
											+ ProcessCard.this.xtp;
									whys[1] = (int) (ProcessCard.this.factp * -scaley
											* (offy + ProcessCard.this.plt2y[ProcessCard.this.tstflag][i]))
											+ ProcessCard.this.ytp;
									Sys.this.processPlatterImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1],
											whys[1]);
								}
								for (i = 1; i <= ProcessCard.this.numpt[ProcessCard.this.tstflag]; ++i) {
									xlabel = (int) (ProcessCard.this.factp * scalex
											* (offx + ProcessCard.this.plt2x[ProcessCard.this.tstflag][i]))
											+ ProcessCard.this.xtp;
									ylabel = (int) (ProcessCard.this.factp * -scaley
											* (offy + ProcessCard.this.plt2y[ProcessCard.this.tstflag][i]) + 7.)
											+ ProcessCard.this.ytp;
									Sys.this.processPlatterImgBuffGraphContext.drawString("*", xlabel, ylabel);
								}
								Sys.this.processPlatterImgBuffGraphContext
										.drawString(String.valueOf(ProcessCard.this.tstflag), xlabel + 10, ylabel);
								// old data
								for (i = 1; i <= 20; ++i) {
									if (ProcessCard.this.plsav[i] == 1) {
										Sys.this.processPlatterImgBuffGraphContext.setColor(Color.yellow);
										exes[1] = (int) (ProcessCard.this.factp * scalex
												* (offx + ProcessCard.this.plt2x[i][1])) + ProcessCard.this.xtp;
										whys[1] = (int) (ProcessCard.this.factp * -scaley
												* (offy + ProcessCard.this.plt2y[i][1])) + ProcessCard.this.ytp;
										for (j = 2; j <= ProcessCard.this.numpt[i]; ++j) {
											exes[0] = exes[1];
											whys[0] = whys[1];
											exes[1] = (int) (ProcessCard.this.factp * scalex
													* (offx + ProcessCard.this.plt2x[i][j])) + ProcessCard.this.xtp;
											whys[1] = (int) (ProcessCard.this.factp * -scaley
													* (offy + ProcessCard.this.plt2y[i][j])) + ProcessCard.this.ytp;
											Sys.this.processPlatterImgBuffGraphContext.drawLine(exes[0], whys[0],
													exes[1], whys[1]);
										}
										for (j = 1; j <= ProcessCard.this.numpt[i]; ++j) {
											xlabel = (int) (ProcessCard.this.factp * scalex
													* (offx + ProcessCard.this.plt2x[i][j])) + ProcessCard.this.xtp;
											ylabel = (int) (ProcessCard.this.factp * -scaley
													* (offy + ProcessCard.this.plt2y[i][j]) + 7.)
													+ ProcessCard.this.ytp;
											Sys.this.processPlatterImgBuffGraphContext.drawString("*", xlabel, ylabel);
										}
										Sys.this.processPlatterImgBuffGraphContext.drawString(String.valueOf(i),
												xlabel + 10, ylabel);
									}
								}

							}
							Sys.this.processPlatterImgBuffGraphContext.setColor(Color.white);
							Sys.this.processPlatterImgBuffGraphContext.fillRect(350, 2, 70, 20);
							Sys.this.processPlatterImgBuffGraphContext.setColor(Color.red);
							Sys.this.processPlatterImgBuffGraphContext.drawString("Save", 365, 17);
						}

						g.drawImage(Sys.this.processPlotterImageBuffer, 0, 0, this);
					}
				}

				/**
				 * (non-Javadoc).
				 *
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run() {
					int timer;

					timer = 100;
					while (true) {
						try {
							Thread.sleep(timer);
						} catch (final InterruptedException e) {
						}
						ProcessCard.this.plotter.repaint();
					}
				}

				/** Start. */
				public void start() {
					if (this.run2 == null) {
						this.run2 = new Thread(this);
						this.run2.start();
					}
					Sys.logger.info("Process Card Process Plotter Started");
				}

				/**
				 * (non-Javadoc).
				 *
				 * @param g
				 *            the g
				 * @see java.awt.Canvas#update(java.awt.Graphics)
				 */
				@Override
				public void update(Graphics g) {
					ProcessCard.this.plotter.paint(g);
				}
			} // Plt

			/** The Class Solvep. */
			class SolveProcess {

				/** Instantiates a new solvep. */
				SolveProcess() {
				}

				/** Gen flow. */
				public void genFlow() { // generate flowfield
					double rnew, thet, psv, fxg;
					int k, index;
					/* all lines of flow except stagnation line */
					for (k = 1; k <= ProcessCard.this.nlnc; ++k) {
						psv = -.5 * (ProcessCard.this.nln2 - 1) + .5 * (k - 1);
						fxg = ProcessCard.this.xflow;
						for (index = 1; index <= ProcessCard.this.nptc; ++index) {
							ProcessCard.this.solvep.getPoints(fxg, psv);
							ProcessCard.this.xg[k][index] = ProcessCard.this.lxgt;
							ProcessCard.this.yg[k][index] = ProcessCard.this.lygt;
							ProcessCard.this.rg[k][index] = ProcessCard.this.lrgt;
							ProcessCard.this.thg[k][index] = ProcessCard.this.lthgt;
							ProcessCard.this.xm[k][index] = ProcessCard.this.lxmt;
							ProcessCard.this.ym[k][index] = ProcessCard.this.lymt;
							if (ProcessCard.this.anflag == 1) { // stall model
								if (ProcessCard.this.alfval > 10.0 && psv > 0.0) {
									if (ProcessCard.this.xm[k][index] > 0.0) {
										ProcessCard.this.ym[k][index] = ProcessCard.this.ym[k][index - 1];
									}
								}
								if (ProcessCard.this.alfval < -10.0 && psv < 0.0) {
									if (ProcessCard.this.xm[k][index] > 0.0) {
										ProcessCard.this.ym[k][index] = ProcessCard.this.ym[k][index - 1];
									}
								}
							}
							ProcessCard.this.solvep.getVel(ProcessCard.this.lrg, ProcessCard.this.lthg);
							fxg = fxg + ProcessCard.this.vxdir * ProcessCard.this.deltb;
							ProcessCard.this.xgc[k][index] = ProcessCard.this.lxgtc;
							ProcessCard.this.ygc[k][index] = ProcessCard.this.lygtc;
						}
					}
					/* stagnation line */
					k = ProcessCard.this.nln2;
					psv = 0.0;
					/* incoming flow */
					for (index = 1; index <= ProcessCard.this.npt2; ++index) {
						rnew = 20.0 - (20.0 - ProcessCard.this.rval)
								* Math.sin(ProcessCard.this.pid2 * (index - 1) / (ProcessCard.this.npt2 - 1));
						thet = Math.asin(.999 * (psv - ProcessCard.this.gamval * Math.log(rnew / ProcessCard.this.rval))
								/ (rnew - ProcessCard.this.rval * ProcessCard.this.rval / rnew));
						fxg = -rnew * Math.cos(thet);
						ProcessCard.this.solvep.getPoints(fxg, psv);
						ProcessCard.this.xg[k][index] = ProcessCard.this.lxgt;
						ProcessCard.this.yg[k][index] = ProcessCard.this.lygt;
						ProcessCard.this.rg[k][index] = ProcessCard.this.lrgt;
						ProcessCard.this.thg[k][index] = ProcessCard.this.lthgt;
						ProcessCard.this.xm[k][index] = ProcessCard.this.lxmt;
						ProcessCard.this.ym[k][index] = ProcessCard.this.lymt;
						ProcessCard.this.xgc[k][index] = ProcessCard.this.lxgtc;
						ProcessCard.this.ygc[k][index] = ProcessCard.this.lygtc;
					}
					/* downstream flow */
					for (index = 1; index <= ProcessCard.this.npt2; ++index) {
						rnew = 20.0 + .01 - (20.0 - ProcessCard.this.rval)
								* Math.cos(ProcessCard.this.pid2 * (index - 1) / (ProcessCard.this.npt2 - 1));
						thet = Math.asin(.999 * (psv - ProcessCard.this.gamval * Math.log(rnew / ProcessCard.this.rval))
								/ (rnew - ProcessCard.this.rval * ProcessCard.this.rval / rnew));
						fxg = rnew * Math.cos(thet);
						ProcessCard.this.solvep.getPoints(fxg, psv);
						ProcessCard.this.xg[k][ProcessCard.this.npt2 + index] = ProcessCard.this.lxgt;
						ProcessCard.this.yg[k][ProcessCard.this.npt2 + index] = ProcessCard.this.lygt;
						ProcessCard.this.rg[k][ProcessCard.this.npt2 + index] = ProcessCard.this.lrgt;
						ProcessCard.this.thg[k][ProcessCard.this.npt2 + index] = ProcessCard.this.lthgt;
						ProcessCard.this.xm[k][ProcessCard.this.npt2 + index] = ProcessCard.this.lxmt;
						ProcessCard.this.ym[k][ProcessCard.this.npt2 + index] = ProcessCard.this.lymt;
						ProcessCard.this.xgc[k][index] = ProcessCard.this.lxgtc;
						ProcessCard.this.ygc[k][index] = ProcessCard.this.lygtc;
					}
					/* stagnation point */
					ProcessCard.this.xg[k][ProcessCard.this.npt2] = ProcessCard.this.xcval;
					ProcessCard.this.yg[k][ProcessCard.this.npt2] = ProcessCard.this.ycval;
					ProcessCard.this.rg[k][ProcessCard.this.npt2] = Math
							.sqrt(ProcessCard.this.xcval * ProcessCard.this.xcval
									+ ProcessCard.this.ycval * ProcessCard.this.ycval);
					ProcessCard.this.thg[k][ProcessCard.this.npt2] = Math.atan2(ProcessCard.this.ycval,
							ProcessCard.this.xcval) / ProcessCard.this.convdr;
					ProcessCard.this.xm[k][ProcessCard.this.npt2] = (ProcessCard.this.xm[k][ProcessCard.this.npt2 + 1]
							+ ProcessCard.this.xm[k][ProcessCard.this.npt2 - 1]) / 2.0;
					ProcessCard.this.ym[k][ProcessCard.this.npt2] = (ProcessCard.this.ym[0][ProcessCard.this.nptc / 4
							+ 1] + ProcessCard.this.ym[0][ProcessCard.this.nptc / 4 * 3 + 1]) / 2.0;
					/* compute lift coefficient */
					ProcessCard.this.leg = ProcessCard.this.xcval
							- Math.sqrt(ProcessCard.this.rval * ProcessCard.this.rval
									- ProcessCard.this.ycval * ProcessCard.this.ycval);
					ProcessCard.this.teg = ProcessCard.this.xcval
							+ Math.sqrt(ProcessCard.this.rval * ProcessCard.this.rval
									- ProcessCard.this.ycval * ProcessCard.this.ycval);
					ProcessCard.this.lem = ProcessCard.this.leg + 1.0 / ProcessCard.this.leg;
					ProcessCard.this.tem = ProcessCard.this.teg + 1.0 / ProcessCard.this.teg;
					ProcessCard.this.chrd = ProcessCard.this.tem - ProcessCard.this.lem;
					ProcessCard.this.clift = ProcessCard.this.gamval * 4.0 * Sys.PI / ProcessCard.this.chrd;

					return;
				}

				/**
				 * Gets the circulation.
				 *
				 * @return the circulation
				 */
				public void getCirculation() { // circulation from Kutta condition

					ProcessCard.this.xcval = 0.0;
					switch (ProcessCard.this.foiltype) {
					case 1: { /* Juokowski geometry */
						juokowskiGeometry();
						break;
					}
					case 2: { /* Elliptical geometry */
						ellipticalGeometry();
						break;
					}
					case 3: { /* Plate geometry */
						plateGeometry();
						break;
					}
					case 4: { /* get circulation for rotating cylinder */
						cylinderGeometry();
						break;
					}
					case 5: { /* get circulation for rotating ball */
						cylinderGeometry();
						break;
					}
					}

					return;
				}

				/**
				 * Cylinder geometry.
				 */
				private void cylinderGeometry() {
					ProcessCard.this.rval = ProcessCard.this.radius / ProcessCard.this.lconv;
					ProcessCard.this.gamval = 4.0 * Sys.PI * Sys.PI * ProcessCard.this.spin * ProcessCard.this.rval
							* ProcessCard.this.rval / (ProcessCard.this.vfsd / ProcessCard.this.vconv);
					ProcessCard.this.gamval = ProcessCard.this.gamval * ProcessCard.this.spindr;
					ProcessCard.this.ycval = .0001;
				}

				/**
				 * Plate geometry.
				 */
				private void plateGeometry() {
					double beta;
					ProcessCard.this.ycval = ProcessCard.this.camval / 2.0;
					ProcessCard.this.rval = Math.sqrt(ProcessCard.this.ycval * ProcessCard.this.ycval + 1.0);
					beta = Math.asin(ProcessCard.this.ycval / ProcessCard.this.rval)
							/ ProcessCard.this.convdr; /* Kutta condition */
					ProcessCard.this.gamval = 2.0 * ProcessCard.this.rval
							* Math.sin((ProcessCard.this.alfval + beta) * ProcessCard.this.convdr);
				}

				/**
				 * Elliptical geometry.
				 */
				private void ellipticalGeometry() {
					double beta;
					ProcessCard.this.ycval = ProcessCard.this.camval / 2.0;
					ProcessCard.this.rval = ProcessCard.this.thkval / 4.0
							+ Math.sqrt(ProcessCard.this.thkval * ProcessCard.this.thkval / 16.0
									+ ProcessCard.this.ycval * ProcessCard.this.ycval + 1.0);
					beta = Math.asin(ProcessCard.this.ycval / ProcessCard.this.rval)
							/ ProcessCard.this.convdr; /* Kutta condition */
					ProcessCard.this.gamval = 2.0 * ProcessCard.this.rval
							* Math.sin((ProcessCard.this.alfval + beta) * ProcessCard.this.convdr);
				}

				/**
				 * Juokowski geometry.
				 */
				private void juokowskiGeometry() {
					double beta;
					ProcessCard.this.ycval = ProcessCard.this.camval / 2.0;
					ProcessCard.this.rval = ProcessCard.this.thkval / 4.0
							+ Math.sqrt(ProcessCard.this.thkval * ProcessCard.this.thkval / 16.0
									+ ProcessCard.this.ycval * ProcessCard.this.ycval + 1.0);
					ProcessCard.this.xcval = 1.0 - Math.sqrt(ProcessCard.this.rval * ProcessCard.this.rval
							- ProcessCard.this.ycval * ProcessCard.this.ycval);
					beta = Math.asin(ProcessCard.this.ycval / ProcessCard.this.rval)
							/ ProcessCard.this.convdr; /* Kutta condition */
					ProcessCard.this.gamval = 2.0 * ProcessCard.this.rval
							* Math.sin((ProcessCard.this.alfval + beta) * ProcessCard.this.convdr);
				}

				/**
				 * Gets the free stream.
				 *
				 * @return the free stream
				 */
				public void getFreeStream() { // free stream conditions
					double hite; /* MODS 19 Jan 00 whole routine */
					double rgas;

					ProcessCard.this.g0 = 32.2;
					rgas = 1718.; /* ft2/sec2 R */
					hite = ProcessCard.this.alt / ProcessCard.this.lconv;
					ProcessCard.this.ps0 = ProcessCard.this.psin / ProcessCard.this.piconv;
					ProcessCard.this.ts0 = ProcessCard.this.tsin / ProcessCard.this.ticonv;
					if (ProcessCard.this.planet == 0) { // Earth standard day
						if (hite <= 36152.) { // Troposphere
							ProcessCard.this.ts0 = 518.6 - 3.56 * hite / 1000.;
							ProcessCard.this.ps0 = 2116.217 * Math.pow(ProcessCard.this.ts0 / 518.6, 5.256);
						}
						if (hite >= 36152. && hite <= 82345.) { // Stratosphere
							ProcessCard.this.ts0 = 389.98;
							ProcessCard.this.ps0 = 2116.217 * .2236 * Math.exp((36000. - hite) / (53.35 * 389.98));
						}
						if (hite >= 82345.) {
							ProcessCard.this.ts0 = 389.98 + 1.645 * (hite - 82345) / 1000.;
							ProcessCard.this.ps0 = 2116.217 * .02456 * Math.pow(ProcessCard.this.ts0 / 389.98, -11.388);
						}
						ProcessCard.this.temf = ProcessCard.this.ts0 - 459.6;
						if (ProcessCard.this.temf <= 0.0) {
							ProcessCard.this.temf = 0.0;
						}
						/*
						 * Eq 1:6A Domasch - effect of humidity rlhum = 0.0 ; presm = ps0 * 29.92 /
						 * 2116.217 ; pvap = rlhum*(2.685+.00353*Math.pow(temf,2.245)); rho = (ps0 -
						 * .379*pvap)/(rgas * ts0) ;
						 */
						ProcessCard.this.rho = ProcessCard.this.ps0 / (rgas * ProcessCard.this.ts0);
					}

					if (ProcessCard.this.planet == 1) { // Mars - curve fit of orbiter data
						rgas = 1149.; /* ft2/sec2 R */
						if (hite <= 22960.) {
							ProcessCard.this.ts0 = 434.02 - .548 * hite / 1000.;
							ProcessCard.this.ps0 = 14.62 * Math.pow(2.71828, -.00003 * hite);
						}
						if (hite > 22960.) {
							ProcessCard.this.ts0 = 449.36 - 1.217 * hite / 1000.;
							ProcessCard.this.ps0 = 14.62 * Math.pow(2.71828, -.00003 * hite);
						}
						ProcessCard.this.rho = ProcessCard.this.ps0 / (rgas * ProcessCard.this.ts0);
					}

					if (ProcessCard.this.planet == 2) { // water -- constant density
						hite = -ProcessCard.this.alt / ProcessCard.this.lconv;
						ProcessCard.this.ts0 = 520.;
						ProcessCard.this.rho = 1.94;
						ProcessCard.this.ps0 = 2116.217 - ProcessCard.this.rho * ProcessCard.this.g0 * hite;
					}

					if (ProcessCard.this.planet == 3) { // specify air temp and pressure
						ProcessCard.this.rho = ProcessCard.this.ps0 / (rgas * ProcessCard.this.ts0);
					}

					if (ProcessCard.this.planet == 4) { // specify fluid density
						ProcessCard.this.ps0 = 2116.217;
					}
					ProcessCard.this.q0 = .5 * ProcessCard.this.rho * ProcessCard.this.vfsd * ProcessCard.this.vfsd
							/ (ProcessCard.this.vconv * ProcessCard.this.vconv);
					ProcessCard.this.pt0 = ProcessCard.this.ps0 + ProcessCard.this.q0;

					return;
				}

				/**
				 * Gets the geom.
				 *
				 * @return the geom
				 */
				public void getGeometry() { // geometry
					double thet, rdm, thtm;
					int index;

					for (index = 1; index <= ProcessCard.this.nptc; ++index) {
						thet = (index - 1) * 360. / (ProcessCard.this.nptc - 1);
						ProcessCard.this.xg[0][index] = ProcessCard.this.rval * Math.cos(ProcessCard.this.convdr * thet)
								+ ProcessCard.this.xcval;
						ProcessCard.this.yg[0][index] = ProcessCard.this.rval * Math.sin(ProcessCard.this.convdr * thet)
								+ ProcessCard.this.ycval;
						ProcessCard.this.rg[0][index] = Math
								.sqrt(ProcessCard.this.xg[0][index] * ProcessCard.this.xg[0][index]
										+ ProcessCard.this.yg[0][index] * ProcessCard.this.yg[0][index]);
						ProcessCard.this.thg[0][index] = Math.atan2(ProcessCard.this.yg[0][index],
								ProcessCard.this.xg[0][index]) / ProcessCard.this.convdr;
						ProcessCard.this.xm[0][index] = (ProcessCard.this.rg[0][index]
								+ 1.0 / ProcessCard.this.rg[0][index])
								* Math.cos(ProcessCard.this.convdr * ProcessCard.this.thg[0][index]);
						ProcessCard.this.ym[0][index] = (ProcessCard.this.rg[0][index]
								- 1.0 / ProcessCard.this.rg[0][index])
								* Math.sin(ProcessCard.this.convdr * ProcessCard.this.thg[0][index]);
						rdm = Math.sqrt(ProcessCard.this.xm[0][index] * ProcessCard.this.xm[0][index]
								+ ProcessCard.this.ym[0][index] * ProcessCard.this.ym[0][index]);
						thtm = Math.atan2(ProcessCard.this.ym[0][index], ProcessCard.this.xm[0][index])
								/ ProcessCard.this.convdr;
						ProcessCard.this.xm[0][index] = rdm
								* Math.cos((thtm - ProcessCard.this.alfval) * ProcessCard.this.convdr);
						ProcessCard.this.ym[0][index] = rdm
								* Math.sin((thtm - ProcessCard.this.alfval) * ProcessCard.this.convdr);
						this.getVel(ProcessCard.this.rval, thet);
						ProcessCard.this.plp[index] = (ProcessCard.this.ps0
								+ ProcessCard.this.pres * ProcessCard.this.q0) / 2116.217 * ProcessCard.this.pconv;
						ProcessCard.this.plv[index] = ProcessCard.this.vel * ProcessCard.this.vfsd;
						ProcessCard.this.xgc[0][index] = ProcessCard.this.rval
								* Math.cos(ProcessCard.this.convdr * thet) + ProcessCard.this.xcval;
						ProcessCard.this.ygc[0][index] = ProcessCard.this.rval
								* Math.sin(ProcessCard.this.convdr * thet) + ProcessCard.this.ycval;
					}

					ProcessCard.this.xt1 = ProcessCard.this.xt + ProcessCard.this.spanfac;
					ProcessCard.this.yt1 = ProcessCard.this.yt - ProcessCard.this.spanfac;
					ProcessCard.this.xt2 = ProcessCard.this.xt - ProcessCard.this.spanfac;
					ProcessCard.this.yt2 = ProcessCard.this.yt + ProcessCard.this.spanfac;

					return;
				}

				/**
				 * Gets the points.
				 *
				 * @param fxg
				 *            the fxg
				 * @param psv
				 *            the psv
				 * @return the points
				 */
				public void getPoints(double fxg, double psv) { // flow in x-psi
					double radm, thetm; /* MODS 20 Jul 99 whole routine */
					double fnew, ynew, yold, rfac, deriv;
					int iter;
					/* get variables in the generating plane */
					/* iterate to find value of yg */
					ynew = 10.0;
					yold = 10.0;
					if (psv < 0.0) {
						ynew = -10.0;
					}
					if (Math.abs(psv) < .001 && ProcessCard.this.alfval < 0.0) {
						ynew = ProcessCard.this.rval;
					}
					if (Math.abs(psv) < .001 && ProcessCard.this.alfval >= 0.0) {
						ynew = -ProcessCard.this.rval;
					}
					fnew = 0.1;
					iter = 1;
					while (Math.abs(fnew) >= .00001 && iter < 25) {
						++iter;
						rfac = fxg * fxg + ynew * ynew;
						if (rfac < ProcessCard.this.rval * ProcessCard.this.rval) {
							rfac = ProcessCard.this.rval * ProcessCard.this.rval + .01;
						}
						fnew = psv - ynew * (1.0 - ProcessCard.this.rval * ProcessCard.this.rval / rfac)
								- ProcessCard.this.gamval * Math.log(Math.sqrt(rfac) / ProcessCard.this.rval);
						deriv = -(1.0 - ProcessCard.this.rval * ProcessCard.this.rval / rfac)
								- 2.0 * ynew * ynew * ProcessCard.this.rval * ProcessCard.this.rval / (rfac * rfac)
								- ProcessCard.this.gamval * ynew / rfac;
						yold = ynew;
						ynew = yold - .5 * fnew / deriv;
					}
					ProcessCard.this.lyg = yold;
					/* rotate for angle of attack */
					ProcessCard.this.lrg = Math.sqrt(fxg * fxg + ProcessCard.this.lyg * ProcessCard.this.lyg);
					ProcessCard.this.lthg = Math.atan2(ProcessCard.this.lyg, fxg) / ProcessCard.this.convdr;
					ProcessCard.this.lxgt = ProcessCard.this.lrg
							* Math.cos(ProcessCard.this.convdr * (ProcessCard.this.lthg + ProcessCard.this.alfval));
					ProcessCard.this.lygt = ProcessCard.this.lrg
							* Math.sin(ProcessCard.this.convdr * (ProcessCard.this.lthg + ProcessCard.this.alfval));
					/* translate cylinder to generate airfoil */
					ProcessCard.this.lxgtc = ProcessCard.this.lxgt = ProcessCard.this.lxgt + ProcessCard.this.xcval;
					ProcessCard.this.lygtc = ProcessCard.this.lygt = ProcessCard.this.lygt + ProcessCard.this.ycval;
					ProcessCard.this.lrgt = Math.sqrt(ProcessCard.this.lxgt * ProcessCard.this.lxgt
							+ ProcessCard.this.lygt * ProcessCard.this.lygt);
					ProcessCard.this.lthgt = Math.atan2(ProcessCard.this.lygt, ProcessCard.this.lxgt)
							/ ProcessCard.this.convdr;
					/* Kutta-Joukowski mapping */
					ProcessCard.this.lxm = (ProcessCard.this.lrgt + 1.0 / ProcessCard.this.lrgt)
							* Math.cos(ProcessCard.this.convdr * ProcessCard.this.lthgt);
					ProcessCard.this.lym = (ProcessCard.this.lrgt - 1.0 / ProcessCard.this.lrgt)
							* Math.sin(ProcessCard.this.convdr * ProcessCard.this.lthgt);
					/* tranforms for view fixed with free stream */
					/* take out rotation for angle of attack mapped and cylinder */
					radm = Math.sqrt(
							ProcessCard.this.lxm * ProcessCard.this.lxm + ProcessCard.this.lym * ProcessCard.this.lym);
					thetm = Math.atan2(ProcessCard.this.lym, ProcessCard.this.lxm) / ProcessCard.this.convdr;
					ProcessCard.this.lxmt = radm
							* Math.cos(ProcessCard.this.convdr * (thetm - ProcessCard.this.alfval));
					ProcessCard.this.lymt = radm
							* Math.sin(ProcessCard.this.convdr * (thetm - ProcessCard.this.alfval));

					ProcessCard.this.lxgt = ProcessCard.this.lxgt - ProcessCard.this.xcval;
					ProcessCard.this.lygt = ProcessCard.this.lygt - ProcessCard.this.ycval;
					ProcessCard.this.lrgt = Math.sqrt(ProcessCard.this.lxgt * ProcessCard.this.lxgt
							+ ProcessCard.this.lygt * ProcessCard.this.lygt);
					ProcessCard.this.lthgt = Math.atan2(ProcessCard.this.lygt, ProcessCard.this.lxgt)
							/ ProcessCard.this.convdr;
					ProcessCard.this.lxgt = ProcessCard.this.lrgt
							* Math.cos((ProcessCard.this.lthgt - ProcessCard.this.alfval) * ProcessCard.this.convdr);
					ProcessCard.this.lygt = ProcessCard.this.lrgt
							* Math.sin((ProcessCard.this.lthgt - ProcessCard.this.alfval) * ProcessCard.this.convdr);

					return;
				}

				/**
				 * Gets the vel.
				 *
				 * @param rad
				 *            the rad
				 * @param theta
				 *            the theta
				 * @return the vel
				 */
				public void getVel(double rad, double theta) { // velocity and pressure
					double ur;
					double uth;
					double jake1;
					double jake2;
					double jakesq;
					double xloc;
					double yloc;
					double thrad;
					double alfrad;

					thrad = ProcessCard.this.convdr * theta;
					alfrad = ProcessCard.this.convdr * ProcessCard.this.alfval;
					/* get x, y location in cylinder plane */
					xloc = rad * Math.cos(thrad);
					yloc = rad * Math.sin(thrad);
					/* velocity in cylinder plane */
					ur = Math.cos(thrad - alfrad) * (1.0 - ProcessCard.this.rval * ProcessCard.this.rval / (rad * rad));
					uth = -Math.sin(thrad - alfrad)
							* (1.0 + ProcessCard.this.rval * ProcessCard.this.rval / (rad * rad))
							- ProcessCard.this.gamval / rad;
					ProcessCard.this.usq = ur * ur + uth * uth;
					ProcessCard.this.vxdir = ur * Math.cos(thrad) - uth * Math.sin(thrad); // MODS 20 Jul 99
					/* translate to generate airfoil */
					xloc = xloc + ProcessCard.this.xcval;
					yloc = yloc + ProcessCard.this.ycval;
					/* compute new radius-theta */
					rad = Math.sqrt(xloc * xloc + yloc * yloc);
					thrad = Math.atan2(yloc, xloc);
					/* compute Joukowski Jacobian */
					jake1 = 1.0 - Math.cos(2.0 * thrad) / (rad * rad);
					jake2 = Math.sin(2.0 * thrad) / (rad * rad);
					jakesq = jake1 * jake1 + jake2 * jake2;
					if (Math.abs(jakesq) <= .01) {
						jakesq = .01; /* protection */
					}
					ProcessCard.this.vsq = ProcessCard.this.usq / jakesq;
					/* vel is velocity ratio - pres is coefficient (p-p0)/q0 */
					if (ProcessCard.this.foiltype <= FOILTYPE_PLATE) {
						ProcessCard.this.vel = Math.sqrt(ProcessCard.this.vsq);
						ProcessCard.this.pres = 1.0 - ProcessCard.this.vsq;
					}
					if (ProcessCard.this.foiltype >= FOILTYPE_CYLINDER) {
						ProcessCard.this.vel = Math.sqrt(ProcessCard.this.usq);
						ProcessCard.this.pres = 1.0 - ProcessCard.this.usq;
					}
					return;
				}

				/** Sets the defaults. */
				public void setDefaults() {
					int i;

					ProcessCard.this.tstflag = 0;
					ProcessCard.this.datp = 0;
					ProcessCard.this.arcor = 0;
					ProcessCard.this.lunits = 0;
					ProcessCard.this.lftout = 0;
					ProcessCard.this.inptopt = 0;
					ProcessCard.this.outopt = 0;
					ProcessCard.this.nummod = 1;
					ProcessCard.this.nlnc = 45;
					ProcessCard.this.nln2 = ProcessCard.this.nlnc / 2 + 1;
					ProcessCard.this.nptc = 75;
					ProcessCard.this.npt2 = ProcessCard.this.nptc / 2 + 1;
					ProcessCard.this.deltb = .5;
					ProcessCard.this.foiltype = FOILTYPE_JUOKOWSKI;
					ProcessCard.this.flflag = 1;
					ProcessCard.this.thkval = .5;
					ProcessCard.this.thkinpt = 12.5; /* MODS 10 SEP 99 */
					ProcessCard.this.camval = 0.0;
					ProcessCard.this.caminpt = 0.0;
					ProcessCard.this.alfval = 0.0;
					ProcessCard.this.gamval = 0.0;
					ProcessCard.this.radius = 1.0;
					ProcessCard.this.spin = 0.0;
					ProcessCard.this.spindr = 1.0;
					ProcessCard.this.rval = 1.0;
					ProcessCard.this.ycval = 0.0;
					ProcessCard.this.xcval = 0.0;
					ProcessCard.this.displ = 1;
					ProcessCard.this.viewflg = WALL_VIEW_SOLID;
					ProcessCard.this.dispp = 2;
					ProcessCard.this.calcrange = 0;
					ProcessCard.this.dout = 0;
					ProcessCard.this.stfact = 1.0;
					// factp = factp * chord/chrdold ;

					ProcessCard.this.xpval = 2.1;
					ProcessCard.this.ypval = -.5;
					ProcessCard.this.pboflag = PROBE_OFF;
					ProcessCard.this.xflow = -20.0; /* MODS 20 Jul 99 */

					ProcessCard.this.pconv = 14.696;
					ProcessCard.this.piconv = 1.0;
					ProcessCard.this.ppconv = 1.0;
					ProcessCard.this.ticonv = 1.0;
					ProcessCard.this.pmin = .5;
					ProcessCard.this.pmax = 1.0;
					ProcessCard.this.fconv = 1.0;
					ProcessCard.this.fmax = 100000.;
					ProcessCard.this.fmaxb = .50;
					ProcessCard.this.vconv = .6818;
					ProcessCard.this.vfsd = 0.0;
					ProcessCard.this.vmax = 250.;
					ProcessCard.this.lconv = 1.0;
					ProcessCard.this.vcon2 = 1.0;
					ProcessCard.this.planet = 3;
					ProcessCard.this.psin = 2116.217;
					ProcessCard.this.psmin = 100;
					ProcessCard.this.psmax = 5000.;
					ProcessCard.this.tsin = 518.6;
					ProcessCard.this.tsmin = 350.;
					ProcessCard.this.tsmax = 660.;

					ProcessCard.this.alt = 0.0;
					ProcessCard.this.altmax = 50000.;
					ProcessCard.this.chrdold = ProcessCard.this.chord = 1.0;
					ProcessCard.this.spnold = ProcessCard.this.span = 3.281;
					ProcessCard.this.aspr = ProcessCard.this.span / ProcessCard.this.chord;
					ProcessCard.this.arold = ProcessCard.this.area = .5 * 3.281;
					ProcessCard.this.armax = 2500.01;
					ProcessCard.this.armin = .01; /* MODS 9 SEP 99 */

					ProcessCard.this.xt = 225;
					ProcessCard.this.yt = 140;
					ProcessCard.this.sldloc = 52;
					ProcessCard.this.xtp = 95;
					ProcessCard.this.ytp = 240;
					ProcessCard.this.factp = 40.0;
					// chrdfac = Math.sqrt(Math.sqrt(chord)); Adds a chord factor to scale the foil
					// according to the chord
					ProcessCard.this.chrdfac = Math.sqrt(ProcessCard.this.chord); // Adds a chord factor to scale the
																					// foil according
					// to the chord
					ProcessCard.this.fact = 32.0 * ProcessCard.this.chrdfac; // chord is the only factor for the fact
																				// variable
					ProcessCard.this.spanfac = (int) (4.0 * 50.0 * .3535);
					ProcessCard.this.xt1 = ProcessCard.this.xt + ProcessCard.this.spanfac;
					ProcessCard.this.yt1 = ProcessCard.this.yt - ProcessCard.this.spanfac;
					ProcessCard.this.xt2 = ProcessCard.this.xt - ProcessCard.this.spanfac;
					ProcessCard.this.yt2 = ProcessCard.this.yt + ProcessCard.this.spanfac;
					ProcessCard.this.plthg[1] = 0.0;
					for (i = 1; i <= 20; ++i) {
						ProcessCard.this.plsav[i] = 0;
					}
					ProcessCard.this.begy = 0.0;
					ProcessCard.this.endy = 10.0;

					ProcessCard.this.probflag = 2;
					ProcessCard.this.anflag = 1;
					ProcessCard.this.vmn = 0.0;
					ProcessCard.this.vmx = 250.0;
					ProcessCard.this.almn = 0.0;
					ProcessCard.this.almx = 50000.0;
					ProcessCard.this.angmn = -20.0;
					ProcessCard.this.angmx = 20.0;
					ProcessCard.this.camn = -25.0;
					ProcessCard.this.camx = 25.0;
					ProcessCard.this.thkmn = 1.0;
					ProcessCard.this.thkmx = 26.0;
					ProcessCard.this.chrdmn = 5. / (2.54 * 12);
					ProcessCard.this.chrdmx = 3.281;
					ProcessCard.this.armn = .01;
					ProcessCard.this.armx = 2500.01;
					ProcessCard.this.spinmn = -1500.0;
					ProcessCard.this.spinmx = 1500.0;
					ProcessCard.this.radmn = .05;
					ProcessCard.this.radmx = 5.0;
					ProcessCard.this.psmn = ProcessCard.this.psmin;
					ProcessCard.this.psmx = ProcessCard.this.psmax;
					ProcessCard.this.tsmn = ProcessCard.this.tsmin;
					ProcessCard.this.tsmx = ProcessCard.this.tsmax;

					ProcessCard.this.laby = Sys.PRESS;
					ProcessCard.this.labyu = Sys.PSI;
					ProcessCard.this.labx = Sys.X2;
					ProcessCard.this.labxu = Sys.CHORD3;
					ProcessCard.this.iprint = 0;

					return;
				}
			} // end Solvep

			/** The Constant serialVersionUID. */
			private static final long serialVersionUID = 1L;

			/** The ang. */
			double ang[][] = new double[21][41];

			/** The endy. */
			double begx;

			/** The endx. */
			double endx;

			/** The begy. */
			double begy;

			/** The endy. */
			double endy;

			/** The arcor. */
			int calcrange;

			/** The arcor. */
			int arcor;

			/** The thkmx. */
			double camn;

			/** The thkmn. */
			double thkmn;

			/** The camx. */
			double camx;

			/** The thkmx. */
			double thkmx;

			/** The spnold. */
			double chord;

			/** The span. */
			double span;

			/** The aspr. */
			double aspr;

			/** The arold. */
			double arold;

			/** The chrdold. */
			double chrdold;

			/** The spnold. */
			double spnold;

			/** The armx. */
			double chrdmn;

			/** The armn. */
			double armn;

			/** The chrdmx. */
			double chrdmx;

			/** The armx. */
			double armx;

			/** The convdr. */
			double convdr = Sys.PI / 180.;

			/** The cop. */
			Cop cop;

			/** The datp. */
			int datp;

			/** The dcam. */
			double dcam[] = new double[41];

			/** The dchrd. */
			double dchrd[] = new double[41];

			/** The xflow. */
			double deltb;

			/** The xflow. */
			double xflow; /* MODS 20 Jul 99 */

			/** The radius. */
			double delx;

			/** The delt. */
			double delt;

			/** The vfsd. */
			double vfsd;

			/** The spin. */
			double spin;

			/** The spindr. */
			double spindr;

			/** The yoff. */
			double yoff;

			/** The radius. */
			double radius;

			/** The dftp. */
			int dftp[] = new int[41];

			/** The sldloc. */
			int displ;

			/** The viewflg. */
			int viewflg;

			/** The dispp. */
			int dispp;

			/** The dout. */
			int dout;

			/** The antim. */
			int antim;

			/** The ancol. */
			int ancol;

			/** The sldloc. */
			int sldloc;

			/** The dlft. */
			double dlft[][] = new double[21][41];

			/** The dlunits. */
			int dlunits[] = new int[41];

			/** The dpin. */
			double dpin[][] = new double[21][41];

			/** The dspan. */
			double dspan[] = new double[41];

			/** The dspd. */
			double dspd[][] = new double[21][41];

			/** The dthk. */
			double dthk[] = new double[41];

			/** The chrdfac. */
			/* plot & probe data */
			double fact;

			/** The xpval. */
			double xpval;

			/** The ypval. */
			double ypval;

			/** The pbval. */
			double pbval;

			/** The factp. */
			double factp;

			/** The chrdfac. */
			double chrdfac;

			/** The planet. */
			int foiltype;

			/** The flflag. */
			int flflag;

			/** The lunits. */
			int lunits;

			/** The lftout. */
			int lftout;

			/** The planet. */
			int planet;

			/** The presm. */
			double g0;

			/** The q 0. */
			double q0;

			/** The ps 0. */
			double ps0;

			/** The pt 0. */
			double pt0;

			/** The ts 0. */
			double ts0;

			/** The rho. */
			double rho;

			/** The rlhum. */
			double rlhum;

			/** The temf. */
			double temf;

			/** The presm. */
			double presm;

			/** The nummod. */
			int inptopt;

			/** The outopt. */
			int outopt;

			/** The iprint. */
			int iprint;

			/** The nummod. */
			int nummod;

			/** The labyu. */
			String labx;

			/** The labxu. */
			String labxu;

			/** The laby. */
			String laby;

			/** The labyu. */
			String labyu;

			/** The layplt. */
			CardLayout layin;

			/** The layout. */
			CardLayout layout;

			/** The layplt. */
			CardLayout layplt;

			/** The tem. */
			double leg;

			/** The teg. */
			double teg;

			/** The lem. */
			double lem;

			/** The tem. */
			double tem;

			/** The nond. */
			int lflag;

			/** The gflag. */
			int gflag;

			/** The plscale. */
			int plscale;

			/** The nond. */
			int nond;

			/** The lft. */
			double lft[][] = new double[21][41];

			/** The ntr. */
			int lines;

			/** The nord. */
			int nord;

			/** The nabs. */
			int nabs;

			/** The ntr. */
			int ntr;

			/** The vxdir. */
			double lxm;

			/** The lym. */
			double lym;

			/** The lxmt. */
			double lxmt;

			/** The lymt. */
			double lymt;

			/** The vxdir. */
			double vxdir;/* MOD 20 Jul */

			/** The lygtc. */
			double lyg;

			/** The lrg. */
			double lrg;

			/** The lthg. */
			double lthg;

			/** The lxgt. */
			double lxgt;

			/** The lygt. */
			double lygt;

			/** The lrgt. */
			double lrgt;

			/** The lthgt. */
			double lthgt;

			/** The lxgtc. */
			double lxgtc;

			/** The lygtc. */
			double lygtc;

			/** The modlnm. */
			int modlnm[] = new int[20];

			/** The anflag. */
			int nptc;

			/** The npt 2. */
			int npt2;

			/** The nlnc. */
			int nlnc;

			/** The nln 2. */
			int nln2;

			/** The rdflag. */
			int rdflag;

			/** The browflag. */
			int browflag;

			/** The probflag. */
			int probflag;

			/** The anflag. */
			int anflag;

			/** The numpt. */
			int numpt[] = new int[20];

			/** The tstflag. */
			int numtest;

			/** The tstflag. */
			int tstflag;

			/** The outerparent. */
			Sys outerparent;

			/** The ytp. */
			int pboflag;

			/** The xt. */
			int xt;

			/** The yt. */
			int yt;

			/** The ntikx. */
			int ntikx;

			/** The ntiky. */
			int ntiky;

			/** The npt. */
			int npt;

			/** The xtp. */
			int xtp;

			/** The ytp. */
			int ytp;

			/** The fmaxb. */
			double pconv;

			/** The pmax. */
			double pmax;

			/** The pmin. */
			double pmin;

			/** The lconv. */
			double lconv;

			/** The rconv. */
			double rconv;

			/** The fconv. */
			double fconv;

			/** The fmax. */
			double fmax;

			/** The fmaxb. */
			double fmaxb;

			/** The ppconv. */
			double piconv;

			/** The ticonv. */
			double ticonv;

			/** The ppconv. */
			double ppconv;

			/** The pid 2. */
			double pid2 = Sys.PI / 2.0;

			/** The pin. */
			double pin[][] = new double[21][41];

			/** The plt. */
			ProcessPlotter plotter;

			/** The plp. */
			double plp[] = new double[80];

			/** The plsav. */
			int plsav[] = new int[21];

			/** The plt 2 x. */
			double plt2x[][] = new double[21][41];

			/** The plt 2 y. */
			double plt2y[][] = new double[21][41];

			/** The plthg. */
			double plthg[] = new double[2];

			/** The plttx. */
			double plttx[][] = new double[3][41];

			/** The pltty. */
			double pltty[][] = new double[3][41];

			/** The pltx. */
			double pltx[][] = new double[3][41];

			/** The plty. */
			double plty[][] = new double[3][41];

			/** The plv. */
			double plv[] = new double[80];

			/** The pypl. */
			double prg;

			/** The pthg. */
			double pthg;

			/** The pxg. */
			double pxg;

			/** The pyg. */
			double pyg;

			/** The pxm. */
			double pxm;

			/** The pym. */
			double pym;

			/** The pxpl. */
			double pxpl;

			/** The pypl. */
			double pypl;

			/** The inp. */
			ProcessInput processInput;

			/** The oup. */
			ProcessOutput processOutput;

			/** The tsmax. */
			double psin;

			/** The tsin. */
			double tsin;

			/** The psmin. */
			double psmin;

			/** The psmax. */
			double psmax;

			/** The tsmin. */
			double tsmin;

			/** The tsmax. */
			double tsmax;

			/** The tsmx. */
			double psmn;

			/** The psmx. */
			double psmx;

			/** The tsmn. */
			double tsmn;

			/** The tsmx. */
			double tsmx;

			/** The qdt. */
			double qdt[][] = new double[21][41];

			/** The spinmx. */
			double radmn;

			/** The spinmn. */
			double spinmn;

			/** The radmx. */
			double radmx;

			/** The spinmx. */
			double spinmx;

			/** The rg. */
			double rg[][] = new double[50][80];

			/** The clift. */
			double rval;

			/** The ycval. */
			double ycval;

			/** The xcval. */
			double xcval;

			/** The gamval. */
			double gamval;

			/** The alfval. */
			double alfval;

			/** The thkval. */
			double thkval;

			/** The camval. */
			double camval;

			/** The chrd. */
			double chrd;

			/** The clift. */
			double clift;

			/** The solvep. */
			SolveProcess solvep;

			/** The spd. */
			double spd[][] = new double[21][41];

			/** The stfact. */
			double stfact;

			/** The testp. */
			int testp[] = new int[20];

			/** The thg. */
			double thg[][] = new double[50][80];

			/** The caminpt. */
			double thkinpt;

			/** The caminpt. */
			double caminpt; /* MODS 10 Sep 99 */

			/** The armin. */
			double usq;

			/** The vsq. */
			double vsq;

			/** The alt. */
			double alt;

			/** The altmax. */
			double altmax;

			/** The area. */
			double area;

			/** The armax. */
			double armax;

			/** The armin. */
			double armin;

			/** The vcon 2. */
			double vconv;

			/** The vmax. */
			double vmax;

			/** The vcon 2. */
			double vcon2;

			/** The angr. */
			double vel;

			/** The pres. */
			double pres;

			/** The lift. */
			double lift;

			/** The side. */
			double side;

			/** The omega. */
			double omega;

			/** The radcrv. */
			double radcrv;

			/** The relsy. */
			double relsy;

			/** The angr. */
			double angr;

			/** The angmx. */
			/* units data */
			double vmn;

			/** The almn. */
			double almn;

			/** The angmn. */
			double angmn;

			/** The vmx. */
			double vmx;

			/** The almx. */
			double almx;

			/** The angmx. */
			double angmx;

			/** The xg. */
			double xg[][] = new double[50][80];

			/** The xgc. */
			double xgc[][] = new double[50][80];

			/** The xm. */
			double xm[][] = new double[50][80];

			/** The xpl. */
			double xpl[][] = new double[50][80];

			/** The xplg. */
			double xplg[][] = new double[50][80];

			/** The spanfac. */
			int xt1;

			/** The yt 1. */
			int yt1;

			/** The xt 2. */
			int xt2;

			/** The yt 2. */
			int yt2;

			/** The spanfac. */
			int spanfac;

			/** The yg. */
			double yg[][] = new double[50][80];

			/** The ygc. */
			double ygc[][] = new double[50][80];

			/** The ym. */
			double ym[][] = new double[50][80];

			/** The ypl. */
			double ypl[][] = new double[50][80];

			/** The yplg. */
			double yplg[][] = new double[50][80];

			/**
			 * Instantiates a new psys.
			 *
			 * @param target
			 *            the target
			 */
			ProcessCard(Sys target) {
				this.setName("ProcessCard");
				Sys.logger.info("Create Process Card");
				this.outerparent = target;
				this.setLayout(new GridLayout(2, 2, 5, 5));

				this.solvep = new SolveProcess();
				this.solvep.setDefaults();

				this.plotter = new ProcessPlotter(this.outerparent);
				this.processOutput = new ProcessOutput(this.outerparent);
				this.processInput = new ProcessInput(this.outerparent);
				this.cop = new Cop(this.outerparent);

				this.add(this.plotter);
				this.add(this.processOutput);
				this.add(this.processInput);
				this.add(this.cop);

				this.solvep.getFreeStream();
				this.computeFlow();
				this.processOutput.view.start();
				this.processOutput.plt2.start();
				this.plotter.start();
			}

			/** Compute flow. */
			public void computeFlow() {

				this.solvep.getFreeStream();

				this.solvep.getCirculation(); /* get circulation */
				this.solvep.getGeometry(); /* get geometry */
				this.solvep.genFlow();

				this.loadOut();

				this.plotter.loadPlot();
			}

			/** Load input. */
			public void loadInput() { // load the input panels
				String outarea, outlngth;

				outlngth = " ft";
				if (this.lunits == UNITS_METERIC) {
					outlngth = " m";
				}
				outarea = " ft^2";
				if (this.lunits == UNITS_METERIC) {
					outarea = " m^2";
				}

				this.aspr = this.span / this.chord;
				this.area = this.span * this.chord;
				this.spanfac = (int) (4.0 * 50.0 * .3535);

				/*
				 * v5 = tsin ; tsmn = tsmin*ticonv ; tsmx = tsmax*ticonv ; fl5 = filter0(v5) ;
				 * inp.flt.upr.inl.f4.setText(String.valueOf(fl5)) ; i5 = (int) (((v5 -
				 * tsmn)/(tsmx-tsmn))*1000.) ; inp.flt.upr.inr.s4.setValue(i5) ;
				 */

				// non-dimensional

				this.cop.camberFld.setText(String.valueOf(Sys.filter3(this.camval * 25.0)));
				this.cop.thickFld.setText(String.valueOf(Sys.filter3(this.thkval * 25.0)));
				this.cop.aspectFld.setText(String.valueOf(Sys.filter3(this.aspr)));
				this.cop.chordFld.setText(Sys.filter3(this.chord) + outlngth);
				this.cop.spanFld.setText(Sys.filter3(this.span) + outlngth);
				this.cop.wingAreaFld.setText(Sys.filter3(this.area) + outarea);

				if (this.foiltype == FOILTYPE_JUOKOWSKI) {
					this.cop.typeFld.setText(Sys.AIRFOIL);
				}
				if (this.foiltype == FOILTYPE_ELLIPTICAL) {
					this.cop.typeFld.setText(Sys.ELLIPSE);
				}
				if (this.foiltype == FOILTYPE_PLATE) {
					this.cop.typeFld.setText(Sys.PLATE);
				}
				// cop.o4.setText(String.valueOf(filter3(clift))) ;
				this.computeFlow();
				return;
			}

			/** Load out. */
			public void loadOut() { // output routine
				String outfor, outden, outpres, outvel;

				outvel = " mph";
				if (this.lunits == UNITS_METERIC) {
					outvel = " km/hr";
				}
				outfor = " lbs";
				if (this.lunits == UNITS_METERIC) {
					outfor = " N";
				}
				outden = " slug/ft^3";
				if (this.lunits == UNITS_METERIC) {
					outden = " kg/m^3";
				}
				outpres = Sys.LBS_FT_2;
				if (this.lunits == UNITS_METERIC) {
					outpres = Sys.K_PA;
				}
				if (this.lunits == UNITS_METERIC) {
				}
				this.area = this.span * this.chord;

				if (this.foiltype <= FOILTYPE_PLATE) { // mapped airfoil
					// stall model
					this.stfact = 1.0;
					if (this.anflag == 1) {
						if (this.alfval > 10.0) {
							this.stfact = .5 + .1 * this.alfval - .005 * this.alfval * this.alfval;
						}
						if (this.alfval < -10.0) {
							this.stfact = .5 - .1 * this.alfval - .005 * this.alfval * this.alfval;
						}
						this.clift = this.clift * this.stfact;
					}

					if (this.arcor == 1) { // correction for low aspect ratio
						this.clift = this.clift / (1.0 + this.clift / (3.14159 * this.aspr));
					}

					this.cop.clCoeffFld.setText(String.valueOf(Sys.filter3(this.clift)));

					if (this.lftout == 0) {
						this.lift = this.clift * this.q0 * this.area / this.lconv / this.lconv; /* lift in lbs */
						this.lift = this.lift * this.fconv;
						if (Math.abs(this.lift) <= 10.0) {
							this.cop.liftFld.setText(Sys.filter3(this.lift) + outfor);
						}
						if (Math.abs(this.lift) > 10.0) {
							this.cop.liftFld.setText(Sys.filter0(this.lift) + outfor);
						}
					}
				}

				switch (this.lunits) {
				case 0: { /* English */
					englishUnits(outden, outpres, outvel);
					break;
				}
				case 1: { /* Metric */
					metricUnits(outden, outpres, outvel);
					break;
				}
				}
				return;
			}

			/**
			 * Metric units.
			 *
			 * @param outden
			 *            the outden
			 * @param outpres
			 *            the outpres
			 * @param outvel
			 *            the outvel
			 */
			private void metricUnits(String outden, String outpres, String outvel) {
				this.cop.desityFld.setText(Sys.filter3(this.rho * 515.4) + outden);
				this.cop.dynPressFld.setText(Sys.filter3(this.q0 * .04788) + outpres);
				this.cop.totalPressFld.setText(Sys.filter3(this.pt0 * .04788) + outpres);
				this.cop.speedFld.setText(Sys.filter3(this.vfsd) + outvel);
				this.cop.staticPressFld.setText(Sys.filter3(this.ps0 * .04788) + outpres);
			}

			/**
			 * English units.
			 *
			 * @param outden
			 *            the outden
			 * @param outpres
			 *            the outpres
			 * @param outvel
			 *            the outvel
			 */
			private void englishUnits(String outden, String outpres, String outvel) {
				this.cop.desityFld.setText(Sys.filter5(this.rho) + outden);
				this.cop.dynPressFld.setText(Sys.filter3(this.q0) + outpres);
				this.cop.totalPressFld.setText(Sys.filter3(this.pt0) + outpres);
				this.cop.speedFld.setText(Sys.filter3(this.vfsd) + outvel);
				this.cop.staticPressFld.setText(Sys.filter3(this.ps0) + outpres);
			}

			/** Reorder. */
			public void reorder() {
				int item, i, nplp;
				double tempx;
				double tempy;

				nplp = this.numpt[this.tstflag];
				for (item = 1; item <= nplp - 1; ++item) {
					for (i = item + 1; i <= nplp; ++i) {
						if (this.plt2x[this.tstflag][i] < this.plt2x[this.tstflag][item]) {
							tempx = this.plt2x[this.tstflag][item];
							tempy = this.plt2y[this.tstflag][item];
							this.plt2x[this.tstflag][item] = this.plt2x[this.tstflag][i];
							this.plt2y[this.tstflag][item] = this.plt2y[this.tstflag][i];
							this.plt2x[this.tstflag][i] = tempx;
							this.plt2y[this.tstflag][i] = tempy;
						}
					}
				}
				return;
			}

			/** Sets the units. */
			public void setUnits() { // Switching Units
				double ovs, chords;
				double spans;
				double aros;
				double chos;
				double spos;
				double rads;
				double alts;
				double ares;
				double pss;
				double tss;

				alts = this.alt / this.lconv;
				chords = this.chord / this.lconv;
				spans = this.span / this.lconv;
				ares = this.area / this.lconv / this.lconv;
				aros = this.arold / this.lconv / this.lconv;
				chos = this.chrdold / this.lconv;
				spos = this.spnold / this.lconv;
				ovs = this.vfsd / this.vconv;
				rads = this.radius / this.lconv;
				pss = this.psin / this.piconv;
				tss = this.tsin / this.ticonv;

				switch (this.lunits) {
				case 0: { /* English */
					englishUnits();
					break;
				}
				case 1: { /* Metric */
					metricUnits();
					break;
				}
				}

				this.psin = pss * this.piconv;
				this.tsin = tss * this.ticonv;
				this.alt = alts * this.lconv;
				this.chord = chords * this.lconv;
				this.span = spans * this.lconv;
				this.area = ares * this.lconv * this.lconv;
				this.arold = aros * this.lconv * this.lconv;
				this.chrdold = chos * this.lconv;
				this.spnold = spos * this.lconv;
				this.vfsd = ovs * this.vconv;
				this.radius = rads * this.lconv;

				return;
			}

			/**
			 * Metric units.
			 */
			private void metricUnits() {
				this.lconv = .3048; /* meters */
				this.vconv = 1.097;
				this.vmax = 400.; /* km/hr */
				if (this.planet == 2) {
					this.vmax = 80.;
				}
				this.vcon2 = 1.609; // mph -> km/hr
				this.fconv = 4.448;
				this.fmax = 500000.;
				this.fmaxb = 2.5; /* newtons */
				this.piconv = .04788; /* kilo-pascals */
				this.ppconv = 47.88; /* n /m^2 */
				this.ticonv = .5555555;
				this.pconv = 101.325; /* kilo-pascals */
			}

			/**
			 * English units.
			 */
			private void englishUnits() {
				this.lconv = 1.; /* feet */
				this.vconv = .6818;
				this.vmax = 250.; /* mph / ft/s */
				if (this.planet == 2) {
					this.vmax = 50.;
				}
				this.vcon2 = 1.0; // mph
				this.fconv = 1.0;
				this.fmax = 100000.;
				this.fmaxb = .5; /* pounds */
				this.piconv = 1.0; /* lb/sq in */
				this.ppconv = 1.0;
				this.ticonv = 1.0;
				this.pconv = 14.696; /* lb/sq in */
			}

		} // end of Psys

		/** The Class Wsys. */
		class WindCard extends Panel {

			/** The Class Conw. */
			class Conw extends Panel {

				/** The Class Dwn. */
				class Dwn extends Panel {

					/** The Constant serialVersionUID. */
					private static final long serialVersionUID = 1L;

					/** The btload. */
					private Button installModelBtn;

					/** The l 10. */
					private Label l1;

					/** The l 2. */
					private Label l2;

					/** The l 3. */
					private Label l3;

					/** The l 4. */
					private Label l4;

					/** The l 5. */
					private Label l5;

					/** The l 6. */
					private Label l6;

					/** The l 7. */
					private Label l7;

					/** The l 8. */
					private Label l8;

					/** The l 9. */
					private Label l9;

					/** The l 10. */
					private Label l10;

					/** The lmod 2. */
					private Label lmod;

					/** The lmod 2. */
					private Label lmod2;

					/** The mod. */
					private TextField mod;

					/** The o 10. */
					private TextField o1;

					/** The o 2. */
					private TextField o2;

					/** The o 3. */
					private TextField o3;

					/** The o 4. */
					private TextField o4;

					/** The o 5. */
					private TextField o5;

					/** The o 6. */
					private TextField o6;

					/** The o 7. */
					private TextField o7;

					/** The o 8. */
					private TextField o8;

					/** The o 9. */
					private TextField o9;

					/** The o 10. */
					private TextField o10;

					/** The outerparent. */
					Sys outerparent;

					/**
					 * Instantiates a new dwn.
					 *
					 * @param target
					 *            the target
					 */
					Dwn(Sys target) {
						this.outerparent = target;
						this.setLayout(new GridLayout(6, 4, 5, 5));

						this.lmod = new Label("Model #", Label.RIGHT);
						this.lmod.setForeground(Color.blue);
						this.lmod2 = new Label("Select->", Label.RIGHT);
						this.lmod2.setForeground(Color.blue);

						this.mod = new TextField("1", 5);
						this.mod.setBackground(Color.white);
						this.mod.setForeground(Color.blue);

						this.installModelBtn = new Button("Install Model");
						this.installModelBtn.setBackground(Color.blue);
						this.installModelBtn.setForeground(Color.white);

						this.l1 = new Label(Sys.CHORD2, Label.CENTER);
						this.o1 = new TextField(String.valueOf(Sys.filter3(WindCard.this.chord)), 5);
						this.o1.setBackground(Color.black);
						this.o1.setForeground(Color.yellow);

						this.l2 = new Label(Sys.CAMBER, Label.CENTER);
						this.o2 = new TextField(Sys.ZERO_ZERO, 5);
						this.o2.setBackground(Color.black);
						this.o2.setForeground(Color.yellow);

						this.l3 = new Label("Span ", Label.CENTER);
						this.o3 = new TextField(String.valueOf(Sys.filter3(WindCard.this.span)), 5);
						this.o3.setBackground(Color.black);
						this.o3.setForeground(Color.yellow);

						this.l4 = new Label(Sys.THICKNESS_SPACE, Label.CENTER);
						this.o4 = new TextField(Sys.TWELVE_FIVE, 5);
						this.o4.setBackground(Color.black);
						this.o4.setForeground(Color.yellow);

						this.l5 = new Label("Aspect Ratio ", Label.CENTER);
						this.o5 = new TextField(String.valueOf(Sys.filter3(WindCard.this.aspr)), 5);
						this.o5.setBackground(Color.black);
						this.o5.setForeground(Color.yellow);

						this.l6 = new Label(Sys.WING_AREA, Label.CENTER);
						this.o6 = new TextField(String.valueOf(Sys.filter3(WindCard.this.area)), 5);
						this.o6.setBackground(Color.black);
						this.o6.setForeground(Color.yellow);

						this.l7 = new Label("Dynamic Press", Label.CENTER);
						this.o7 = new TextField(Sys.TWELVE_FIVE, 5);
						this.o7.setBackground(Color.black);
						this.o7.setForeground(Color.yellow);

						this.l8 = new Label("Total Pressure ", Label.CENTER);
						this.o8 = new TextField("1.5", 5);
						this.o8.setBackground(Color.black);
						this.o8.setForeground(Color.yellow);

						this.l9 = new Label(Sys.DENSITY, Label.CENTER);
						this.o9 = new TextField(".00237", 5);
						this.o9.setBackground(Color.black);
						this.o9.setForeground(Color.yellow);
						this.l10 = new Label("Lift ", Label.CENTER);
						this.o10 = new TextField(Sys.TWELVE_FIVE, 5);
						this.o10.setBackground(Color.black);
						this.o10.setForeground(Color.green);

						this.add(this.l9);
						this.add(this.o9);
						this.add(this.l10);
						this.add(this.o10);

						this.add(this.l7);
						this.add(this.o7);
						this.add(this.l8);
						this.add(this.o8);

						this.add(this.lmod2);
						this.add(this.lmod);
						this.add(this.mod);
						this.add(this.installModelBtn);

						this.add(this.l1);
						this.add(this.o1);
						this.add(this.l2);
						this.add(this.o2);

						this.add(this.l3);
						this.add(this.o3);
						this.add(this.l4);
						this.add(this.o4);

						this.add(this.l5);
						this.add(this.o5);
						this.add(this.l6);
						this.add(this.o6);
					}

					/**
					 * (non-Javadoc).
					 *
					 * @param evt
					 *            the evt
					 * @param arg
					 *            the arg
					 * @return true, if successful
					 * @see java.awt.Component#action(java.awt.Event, java.lang.Object)
					 */
					@Override
					public boolean action(Event evt, Object arg) {
						if (evt.target instanceof Button) {
							this.handleRefs(evt, arg);
							return true;
						} else {
							return false;
						}
					} // handler

					/**
					 * Handle refs.
					 *
					 * @param evt
					 *            the evt
					 * @param arg
					 *            the arg
					 */
					public void handleRefs(Event evt, Object arg) {
						final String label = (String) arg;
						Double V1;
						double v1;
						int i1;

						if (label.equals("Install Model")) {
							V1 = Double.valueOf(this.mod.getText());
							v1 = V1.doubleValue();
							i1 = (int) v1;

							if (i1 > WindCard.this.nummod) {
								i1 = WindCard.this.nummod;
								this.mod.setText(String.valueOf(i1));
							}
							WindCard.this.foiltype = WindCard.this.dftp[i1];
							WindCard.this.lunits = WindCard.this.dlunits[i1];
							WindCard.this.setUnits();
							if (WindCard.this.lunits == UNITS_ENGLISH) {
								WindCard.this.inw.flt.lwr.untch.select(0);
							}
							if (WindCard.this.lunits == UNITS_METERIC) {
								WindCard.this.inw.flt.lwr.untch.select(1);
							}
							WindCard.this.camval = WindCard.this.dcam[i1];
							WindCard.this.thkval = WindCard.this.dthk[i1];
							WindCard.this.span = WindCard.this.dspan[i1];
							WindCard.this.chord = WindCard.this.dchrd[i1];
							WindCard.this.chrdfac = Math.sqrt(WindCard.this.chord / WindCard.this.lconv);
							WindCard.this.fact = 32.0 * WindCard.this.chrdfac;

							WindCard.this.loadInput();
						}
					}
				} // end Dwn

				/** The Class Prb. */
				class Probe extends Panel {

					/** The Class L. */
					class L extends Panel {

						/** The Constant serialVersionUID. */
						private static final long serialVersionUID = 1L;

						/** The bt 4. */
						private Button probeOnBtn;

						/** The bt 1. */
						private Button velocityBtn;

						/** The bt 2. */
						private Button pressureBtn;

						/** The bt 3. */
						private Button smokeBtn;

						/** The bt 4. */
						private Button probeOffBtn;

						/** The outerparent. */
						Sys outerparent;

						/**
						 * Instantiates a new l.
						 *
						 * @param target
						 *            the target
						 */
						L(Sys target) {
							this.outerparent = target;
							this.setLayout(new GridLayout(5, 1, 2, 2));

							this.probeOnBtn = new Button("Probe ON");
							this.probeOnBtn.setBackground(Color.white);
							this.probeOnBtn.setForeground(Color.blue);

							this.velocityBtn = new Button(Sys.VELOCITY);
							this.velocityBtn.setBackground(Color.white);
							this.velocityBtn.setForeground(Color.blue);

							this.pressureBtn = new Button(Sys.PRESSURE);
							this.pressureBtn.setBackground(Color.white);
							this.pressureBtn.setForeground(Color.blue);

							this.smokeBtn = new Button("Smoke");
							this.smokeBtn.setBackground(Color.white);
							this.smokeBtn.setForeground(Color.blue);

							this.probeOffBtn = new Button("Probe OFF");
							this.probeOffBtn.setBackground(Color.red);
							this.probeOffBtn.setForeground(Color.white);

							this.add(this.probeOnBtn);
							this.add(this.velocityBtn);
							this.add(this.pressureBtn);
							this.add(this.smokeBtn);
							this.add(this.probeOffBtn);
						}

						/**
						 * (non-Javadoc).
						 *
						 * @param evt
						 *            the evt
						 * @param arg
						 *            the arg
						 * @return true, if successful
						 * @see java.awt.Component#action(java.awt.Event, java.lang.Object)
						 */
						@Override
						public boolean action(Event evt, Object arg) {
							if (evt.target instanceof Button) {
								final String label = (String) arg;
								if (label.equals("Probe ON")) {
									WindCard.this.pboflag = PROBE_VELOCITY;
									colorProbeBtnOn();
								}
								if (label.equals(Sys.VELOCITY)) {
									WindCard.this.pboflag = PROBE_VELOCITY;
									colorProbeBtnOn();
								}
								if (label.equals(Sys.PRESSURE)) {
									WindCard.this.pboflag = PROBE_PRESSURE;
									colorProbeBtnOn();
								}
								if (label.equals("Smoke")) {
									WindCard.this.pboflag = PROBLE_SMOKE;
									colorProbeBtnOn();
								}
								if (label.equals("Probe OFF")) {
									WindCard.this.pboflag = PROBE_OFF;
									colorProbeBtnOff();
								}

								WindCard.this.computeFlow();
								return true;
							} else {
								return false;
							}
						} // Handler

						/**
						 * Color probe btn off.
						 */
						private void colorProbeBtnOff() {
							this.probeOnBtn.setBackground(Color.white);
							this.probeOnBtn.setForeground(Color.blue);
							this.probeOffBtn.setBackground(Color.red);
							this.probeOffBtn.setForeground(Color.white);
						}

						/**
						 * Color probe btn on.
						 */
						private void colorProbeBtnOn() {
							this.probeOnBtn.setBackground(Color.green);
							this.probeOnBtn.setForeground(Color.black);
							this.probeOffBtn.setBackground(Color.white);
							this.probeOffBtn.setForeground(Color.red);
						}
					} // Inl

					/** The Class R. */
					class R extends Panel {

						/** The Class L2. */
						class L2 extends Canvas {

							/** The Constant serialVersionUID. */
							private static final long serialVersionUID = 1L;

							/**
							 * The outerparent.
							 *
							 * @param target
							 *            the target
							 */
							// private Sys outerparent;

							/**
							 * Instantiates a new l2.
							 *
							 * @param target
							 *            the target
							 */
							L2(Sys target) {
								this.setBackground(Color.black);
							}

							/**
							 * (non-Javadoc).
							 *
							 * @param g
							 *            the g
							 * @see java.awt.Canvas#paint(java.awt.Graphics)
							 */
							@Override
							public void paint(Graphics g) {
								final boolean b = true;
								if (b && Sys.this.mainPannel.windCard.isVisible()) {
									int ex, ey, index;
									double pbout;

									Sys.this.probeL2ImgBuffGraphContext.setColor(Color.black);
									Sys.this.probeL2ImgBuffGraphContext.fillRect(0, 0, Sys.PROBE_L2_WIDTH,
											Sys.PROBE_L2_HEIGHT);

									if (WindCard.this.pboflag == PROBE_OFF || WindCard.this.pboflag == PROBLE_SMOKE) {
										Sys.this.probeL2ImgBuffGraphContext.setColor(Color.gray);
									}
									if (WindCard.this.pboflag == PROBE_VELOCITY
											|| WindCard.this.pboflag == PROBE_PRESSURE) {
										Sys.this.probeL2ImgBuffGraphContext.setColor(Color.yellow);
									}
									Sys.this.probeL2ImgBuffGraphContext.fillArc(40, 30, 80, 80, -23, 227);
									Sys.this.probeL2ImgBuffGraphContext.setColor(Color.black);
									// tick marks
									for (index = 1; index <= 4; ++index) {
										ex = 80 + (int) (50.0
												* Math.cos(WindCard.this.convdr * (-22.5 + 45.0 * index)));
										ey = 70 - (int) (50.0
												* Math.sin(WindCard.this.convdr * (-22.5 + 45.0 * index)));
										Sys.this.probeL2ImgBuffGraphContext.drawLine(60, 70, ex, ey);
									}
									Sys.this.probeL2ImgBuffGraphContext.fillArc(45, 35, 70, 70, -25, 235);

									Sys.this.probeL2ImgBuffGraphContext.setColor(Color.yellow);
									Sys.this.probeL2ImgBuffGraphContext.drawString("0", 30, 95);
									Sys.this.probeL2ImgBuffGraphContext.drawString("2", 30, 55);
									Sys.this.probeL2ImgBuffGraphContext.drawString("4", 55, 30);
									Sys.this.probeL2ImgBuffGraphContext.drawString("6", 95, 30);
									Sys.this.probeL2ImgBuffGraphContext.drawString("8", 120, 55);
									Sys.this.probeL2ImgBuffGraphContext.drawString("10", 120, 95);

									Sys.this.probeL2ImgBuffGraphContext.setColor(Color.green);
									if (WindCard.this.pboflag == PROBE_VELOCITY) {
										Sys.this.probeL2ImgBuffGraphContext.drawString(Sys.VELOCITY, 60, 15);
										if (WindCard.this.lunits == UNITS_ENGLISH) {
											Sys.this.probeL2ImgBuffGraphContext.drawString(Sys.MPH, 70, 125);
										}
										if (WindCard.this.lunits == UNITS_METERIC) {
											Sys.this.probeL2ImgBuffGraphContext.drawString("km/h", 70, 125);
										}
									}
									if (WindCard.this.pboflag == PROBE_PRESSURE) {
										Sys.this.probeL2ImgBuffGraphContext.drawString(Sys.PRESSURE, 50, 15);
										if (WindCard.this.lunits == UNITS_ENGLISH) {
											Sys.this.probeL2ImgBuffGraphContext.drawString(Sys.PSI, 70, 125);
										}
										if (WindCard.this.lunits == UNITS_METERIC) {
											Sys.this.probeL2ImgBuffGraphContext.drawString("kPa", 70, 125);
										}
									}

									Sys.this.probeL2ImgBuffGraphContext.setColor(Color.green);
									Sys.this.probeL2ImgBuffGraphContext.drawString("x 10", 85, 110);

									ex = 80;
									ey = 70;

									pbout = 0.0;
									if (WindCard.this.pbval <= .001) {
										pbout = WindCard.this.pbval * 1000.;
										Sys.this.probeL2ImgBuffGraphContext.drawString("-4", 110, 105);
									}
									if (WindCard.this.pbval <= .01 && WindCard.this.pbval > .001) {
										pbout = WindCard.this.pbval * 100.;
										Sys.this.probeL2ImgBuffGraphContext.drawString("-3", 110, 105);
									}
									if (WindCard.this.pbval <= .1 && WindCard.this.pbval > .01) {
										pbout = WindCard.this.pbval * 10.;
										Sys.this.probeL2ImgBuffGraphContext.drawString("-2", 110, 105);
									}
									if (WindCard.this.pbval <= 1 && WindCard.this.pbval > .1) {
										pbout = WindCard.this.pbval * 10.;
										Sys.this.probeL2ImgBuffGraphContext.drawString("-1", 110, 105);
									}
									if (WindCard.this.pbval <= 10 && WindCard.this.pbval > 1) {
										pbout = WindCard.this.pbval;
										Sys.this.probeL2ImgBuffGraphContext.drawString("0", 110, 105);
									}
									if (WindCard.this.pbval <= 100 && WindCard.this.pbval > 10) {
										pbout = WindCard.this.pbval * .1;
										Sys.this.probeL2ImgBuffGraphContext.drawString("1", 110, 105);
									}
									if (WindCard.this.pbval <= 1000 && WindCard.this.pbval > 100) {
										pbout = WindCard.this.pbval * .01;
										Sys.this.probeL2ImgBuffGraphContext.drawString("2", 110, 105);
									}
									if (WindCard.this.pbval > 1000) {
										pbout = WindCard.this.pbval * .001;
										Sys.this.probeL2ImgBuffGraphContext.drawString("3", 110, 105);
									}
									Sys.this.probeL2ImgBuffGraphContext.setColor(Color.green);
									Sys.this.probeL2ImgBuffGraphContext.drawString(String.valueOf(Sys.filter3(pbout)),
											50, 110);

									Sys.this.probeL2ImgBuffGraphContext.setColor(Color.yellow);
									ex = 80 - (int) (30.0
											* Math.cos(WindCard.this.convdr * (-22.5 + pbout * 225. / 10.0)));
									ey = 70 - (int) (30.0
											* Math.sin(WindCard.this.convdr * (-22.5 + pbout * 225. / 10.0)));
									Sys.this.probeL2ImgBuffGraphContext.drawLine(80, 70, ex, ey);

									g.drawImage(Sys.this.probeL2ImageBuffer, 0, 0, this);
								}
							}

							/**
							 * (non-Javadoc).
							 *
							 * @param g
							 *            the g
							 * @see java.awt.Canvas#update(java.awt.Graphics)
							 */
							@Override
							public void update(Graphics g) {
								WindCard.this.conw.probe.r.l2.paint(g);
							}
						} // L2

						/** The Constant serialVersionUID. */
						private static final long serialVersionUID = 1L;

						/** The l 2. */
						L2 l2;

						/** The outerparent. */
						Sys outerparent;

						/** The s 2. */
						Scrollbar s1, s2;

						/**
						 * Instantiates a new r.
						 *
						 * @param target
						 *            the target
						 */
						R(Sys target) {

							this.outerparent = target;
							this.setLayout(new BorderLayout(5, 5));

							this.s1 = new Scrollbar(Scrollbar.VERTICAL, 550, 10, 0, 1000);
							this.s2 = new Scrollbar(Scrollbar.HORIZONTAL, 550, 10, 0, 1000);

							this.l2 = new L2(this.outerparent);

							this.add(BorderLayout.WEST, this.s1);
							this.add(BorderLayout.SOUTH, this.s2);
							this.add(BorderLayout.CENTER, this.l2);
						}

						/**
						 * Handle bar.
						 *
						 * @param evt
						 *            the evt
						 */
						public void handleBar(Event evt) {
							int i1, i2;

							i1 = this.s1.getValue();
							i2 = this.s2.getValue();

							WindCard.this.ypval = 5.0 - i1 * 10.0 / 1000.;
							WindCard.this.xpval = i2 * 20.0 / 1000. - 10.0;

							WindCard.this.computeFlow();
						}

						/**
						 * (non-Javadoc).
						 *
						 * @param evt
						 *            the evt
						 * @return true, if successful
						 * @see java.awt.Component#handleEvent(java.awt.Event)
						 */
						@Override
						public boolean handleEvent(Event evt) {
							if (evt.id == Event.SCROLL_ABSOLUTE) {
								this.handleBar(evt);
								return true;
							}
							if (evt.id == Event.SCROLL_LINE_DOWN) {
								this.handleBar(evt);
								return true;
							}
							if (evt.id == Event.SCROLL_LINE_UP) {
								this.handleBar(evt);
								return true;
							}
							if (evt.id == Event.SCROLL_PAGE_DOWN) {
								this.handleBar(evt);
								return true;
							}
							if (evt.id == Event.SCROLL_PAGE_UP) {
								this.handleBar(evt);
								return true;
							} else {
								return false;
							}
						}
					} // Inr

					/** The Constant serialVersionUID. */
					private static final long serialVersionUID = 1L;

					/** The l. */
					L l;

					/** The outerparent. */
					Sys outerparent;

					/** The r. */
					R r;

					/**
					 * Instantiates a new prb.
					 *
					 * @param target
					 *            the target
					 */
					Probe(Sys target) {

						this.outerparent = target;
						this.setLayout(new GridLayout(1, 2, 5, 5));

						this.l = new L(this.outerparent);
						this.r = new R(this.outerparent);

						this.add(this.l);
						this.add(this.r);
					}
				} // Prb

				/** The Constant serialVersionUID. */
				private static final long serialVersionUID = 1L;

				/** The dwn. */
				Dwn dwn;

				/** The outerparent. */
				Sys outerparent;

				/** The prb. */
				Probe probe;

				/**
				 * Instantiates a new conw.
				 *
				 * @param target
				 *            the target
				 */
				Conw(Sys target) {
					this.outerparent = target;
					this.setLayout(new GridLayout(2, 1, 5, 5));

					this.probe = new Probe(this.outerparent);
					this.dwn = new Dwn(this.outerparent);

					this.add(this.probe);
					this.add(this.dwn);
				}
			} // Conw

			/** The Class Inw. */
			class Inw extends Panel {

				/** The Class Cyl. */
				class Cyl extends Panel {

					/** The Class Inl. */
					class Inl extends Panel {

						/** The Constant serialVersionUID. */
						private static final long serialVersionUID = 1L;

						/** The f 3. */
						private TextField f1;

						/** The f 2. */
						private TextField f2;

						/** The f 3. */
						private TextField f3;

						/** The l 02. */
						private Label l01;

						/** The l 02. */
						private Label l02;

						/** The l 3. */
						private Label l1;

						/** The l 2. */
						private Label l2;

						/** The l 3. */
						private Label l3;

						/** The outerparent. */
						Sys outerparent;

						/**
						 * Instantiates a new inl.
						 *
						 * @param target
						 *            the target
						 */
						Inl(Sys target) {

							this.outerparent = target;
							this.setLayout(new GridLayout(5, 2, 2, 10));

							this.l01 = new Label("Cylinder-", Label.RIGHT);
							this.l01.setForeground(Color.blue);

							this.l02 = new Label("Ball Input", Label.LEFT);
							this.l02.setForeground(Color.blue);

							this.l1 = new Label("Spin rpm", Label.CENTER);
							this.f1 = new TextField(Sys.ZERO_ZERO, 5);

							this.l2 = new Label(Sys.RADIUS_FT, Label.CENTER);
							this.f2 = new TextField(".5", 5);

							this.l3 = new Label("Span ft", Label.CENTER);
							this.f3 = new TextField("5.0", 5);

							this.add(this.l01);
							this.add(this.l02);
							this.add(this.l1);
							this.add(this.f1);

							this.add(this.l2);
							this.add(this.f2);

							this.add(this.l3);
							this.add(this.f3);

							this.add(new Label(Sys.SPACE_STR, Label.CENTER));
							this.add(new Label(Sys.SPACE_STR, Label.CENTER));
						}

						/**
						 * (non-Javadoc).
						 *
						 * @param evt
						 *            the evt
						 * @return true, if successful
						 * @see java.awt.Component#handleEvent(java.awt.Event)
						 */
						@Override
						public boolean handleEvent(Event evt) {
							Double V1, V2, V3;
							double v1;
							double v2;
							double v3;
							float fl1;
							int i1, i2;
							if (evt.id == Event.ACTION_EVENT) {
								V1 = Double.valueOf(this.f1.getText());
								v1 = V1.doubleValue();
								V2 = Double.valueOf(this.f2.getText());
								v2 = V2.doubleValue();
								V3 = Double.valueOf(this.f3.getText());
								v3 = V3.doubleValue();

								WindCard.this.spin = v1;
								if (v1 < WindCard.this.spinmn) {
									WindCard.this.spin = v1 = WindCard.this.spinmn;
									fl1 = (float) v1;
									this.f1.setText(String.valueOf(fl1));
								}
								if (v1 > WindCard.this.spinmx) {
									WindCard.this.spin = v1 = WindCard.this.spinmx;
									fl1 = (float) v1;
									this.f1.setText(String.valueOf(fl1));
								}
								WindCard.this.spin = WindCard.this.spin / 60.0;

								WindCard.this.radius = v2;
								if (v2 < WindCard.this.radmn) {
									WindCard.this.radius = v2 = WindCard.this.radmn;
									fl1 = (float) v2;
									this.f2.setText(String.valueOf(fl1));
								}
								if (v2 > WindCard.this.radmx) {
									WindCard.this.radius = v2 = WindCard.this.radmx;
									fl1 = (float) v2;
									this.f2.setText(String.valueOf(fl1));
								}
								Inw.this.cyl.setLims();

								v3 = WindCard.this.span;
								if (WindCard.this.foiltype == FOILTYPE_BALL) {
									WindCard.this.radius = WindCard.this.span;
									fl1 = (float) v3;
									this.f3.setText(String.valueOf(fl1));
								}
								WindCard.this.spanfac = (int) (WindCard.this.fact * WindCard.this.span
										/ WindCard.this.radius * .3535);
								WindCard.this.area = 2.0 * WindCard.this.radius * WindCard.this.span;
								if (WindCard.this.foiltype == FOILTYPE_BALL) {
									WindCard.this.area = Sys.PI * WindCard.this.radius * WindCard.this.radius;
								}

								i1 = (int) ((v1 - WindCard.this.spinmn) / (WindCard.this.spinmx - WindCard.this.spinmn)
										* 1000.);
								i2 = (int) ((v2 - WindCard.this.radmn) / (WindCard.this.radmx - WindCard.this.radmn)
										* 1000.);
								Sys.MainPanel.WindCard.Inw.Cyl.this.inr.s1.setValue(i1);
								Sys.MainPanel.WindCard.Inw.Cyl.this.inr.s2.setValue(i2);

								WindCard.this.computeFlow();
								return true;
							} else {
								return false;
							}
						} // Handler
					} // Inl

					/** The Class Inr. */
					class Inr extends Panel {

						/** The Constant serialVersionUID. */
						private static final long serialVersionUID = 1L;

						/** The outerparent. */
						// private Sys outerparent;

						/** The s 2. */
						private Scrollbar s1;

						/** The s 2. */
						private Scrollbar s2;

						/** The shapch. */
						private Choice shapch;

						/**
						 * Instantiates a new inr.
						 *
						 * @param target
						 *            the target
						 */
						Inr(Sys target) {
							int i1, i2;
							// this.outerparent = target;
							this.setLayout(new GridLayout(5, 1, 2, 10));

							i1 = (int) ((WindCard.this.spin * 60.0 - WindCard.this.spinmn)
									/ (WindCard.this.spinmx - WindCard.this.spinmn) * 1000.);
							i2 = (int) ((WindCard.this.radius - WindCard.this.radmn)
									/ (WindCard.this.radmx - WindCard.this.radmn) * 1000.);

							this.s1 = new Scrollbar(Scrollbar.HORIZONTAL, i1, 10, 0, 1000);
							this.s2 = new Scrollbar(Scrollbar.HORIZONTAL, i2, 10, 0, 1000);

							this.shapch = new Choice();
							this.shapch.addItem(Sys.AIRFOIL);
							this.shapch.addItem(Sys.ELLIPSE);
							this.shapch.addItem(Sys.PLATE);
							this.shapch.addItem("Cylinder");
							this.shapch.addItem("Ball");
							this.shapch.setBackground(Color.white);
							this.shapch.setForeground(Color.blue);
							this.shapch.select(0);

							this.add(this.shapch);
							this.add(this.s1);
							this.add(this.s2);
							this.add(new Label(Sys.SPACE_STR, Label.CENTER));
						}

						/**
						 * Handle bar.
						 *
						 * @param evt
						 *            the evt
						 */
						public void handleBar(Event evt) {
							int i1, i2;
							double v1;
							double v2;
							double v3;
							float fl1, fl2, fl3;

							// Input for computations
							i1 = this.s1.getValue();
							i2 = this.s2.getValue();

							WindCard.this.spin = v1 = i1 * (WindCard.this.spinmx - WindCard.this.spinmn) / 1000.
									+ WindCard.this.spinmn;
							WindCard.this.spin = WindCard.this.spin / 60.0;
							WindCard.this.radius = v2 = i2 * (WindCard.this.radmx - WindCard.this.radmn) / 1000.
									+ WindCard.this.radmn;
							v3 = WindCard.this.span;
							if (WindCard.this.foiltype == FOILTYPE_BALL) {
								WindCard.this.radius = v3;
							}
							WindCard.this.spanfac = (int) (WindCard.this.fact * WindCard.this.span
									/ WindCard.this.radius * .3535);
							WindCard.this.area = 2.0 * WindCard.this.radius * WindCard.this.span;
							if (WindCard.this.foiltype == FOILTYPE_BALL) {
								WindCard.this.area = Sys.PI * WindCard.this.radius * WindCard.this.radius;
							}
							Inw.this.cyl.setLims();

							fl1 = (float) v1;
							fl2 = (float) v2;
							fl3 = (float) v3;

							Sys.MainPanel.WindCard.Inw.Cyl.this.inl.f1.setText(String.valueOf(fl1));
							Sys.MainPanel.WindCard.Inw.Cyl.this.inl.f2.setText(String.valueOf(fl2));
							Sys.MainPanel.WindCard.Inw.Cyl.this.inl.f3.setText(String.valueOf(fl3));

							WindCard.this.computeFlow();
						}

						/**
						 * Handle cho.
						 *
						 * @param evt
						 *            the evt
						 */
						public void handleCho(Event evt) {
							WindCard.this.foiltype = this.shapch.getSelectedIndex() + 1;
							if (WindCard.this.foiltype >= FOILTYPE_CYLINDER) {
								WindCard.this.alfval = 0.0;
							}
							if (WindCard.this.foiltype <= FOILTYPE_ELLIPTICAL) {
								WindCard.this.layin.show(WindCard.this.inw, MainPanel.SECOND_CARD);
							}
							if (WindCard.this.foiltype == FOILTYPE_PLATE) {
								WindCard.this.layin.show(WindCard.this.inw, MainPanel.SECOND_CARD);
								WindCard.this.thkinpt = WindCard.this.thkmn;
								WindCard.this.thkval = WindCard.this.thkinpt / 25.0;
							}
							if (WindCard.this.foiltype == FOILTYPE_CYLINDER) {
								WindCard.this.layin.show(WindCard.this.inw, MainPanel.FOURTH_CARD);
							}
							if (WindCard.this.foiltype == FOILTYPE_BALL) {
								WindCard.this.radius = WindCard.this.span;
								WindCard.this.area = Sys.PI * WindCard.this.radius * WindCard.this.radius;
								WindCard.this.layin.show(WindCard.this.inw, MainPanel.FOURTH_CARD);
								if (WindCard.this.viewflg != WALL_VIEW_TRANSPARENT) {
									WindCard.this.viewflg = WALL_VIEW_TRANSPARENT;
								}
							}

							if (WindCard.this.foiltype <= FOILTYPE_PLATE) {
								if (WindCard.this.planet <= FOILTYPE_JUOKOWSKI) {
									WindCard.this.layplt.show(WindCard.this.ouw.grf.l, MainPanel.FIRST_CARD);
								}
								if (WindCard.this.planet >= FOILTYPE_ELLIPTICAL) {
									WindCard.this.layplt.show(WindCard.this.ouw.grf.l, MainPanel.SECOND_CARD);
								}
							}
							if (WindCard.this.foiltype >= FOILTYPE_CYLINDER) {
								WindCard.this.layplt.show(WindCard.this.ouw.grf.l, MainPanel.SECOND_CARD);
							}

							WindCard.this.layout.show(WindCard.this.ouw, MainPanel.FIRST_CARD);
							WindCard.this.outopt = 0;
							WindCard.this.dispp = 0;
							WindCard.this.calcrange = 0;

							WindCard.this.loadInput();
						} // handler

						/**
						 * (non-Javadoc).
						 *
						 * @param evt
						 *            the evt
						 * @return true, if successful
						 * @see java.awt.Component#handleEvent(java.awt.Event)
						 */
						@Override
						public boolean handleEvent(Event evt) {
							if (evt.id == Event.ACTION_EVENT) {
								this.handleCho(evt);
								return true;
							}
							if (evt.id == Event.SCROLL_ABSOLUTE) {
								this.handleBar(evt);
								return true;
							}
							if (evt.id == Event.SCROLL_LINE_DOWN) {
								this.handleBar(evt);
								return true;
							}
							if (evt.id == Event.SCROLL_LINE_UP) {
								this.handleBar(evt);
								return true;
							}
							if (evt.id == Event.SCROLL_PAGE_DOWN) {
								this.handleBar(evt);
								return true;
							}
							if (evt.id == Event.SCROLL_PAGE_UP) {
								this.handleBar(evt);
								return true;
							} else {
								return false;
							}
						}
					} // Inr

					/** The Constant serialVersionUID. */
					private static final long serialVersionUID = 1L;

					/** The inl. */
					Inl inl;

					/** The inr. */
					Inr inr;

					/** The outerparent. */
					Sys outerparent;

					/**
					 * Instantiates a new cyl.
					 *
					 * @param target
					 *            the target
					 */
					Cyl(Sys target) {

						this.outerparent = target;
						this.setLayout(new GridLayout(1, 2, 5, 5));

						this.inl = new Inl(this.outerparent);
						this.inr = new Inr(this.outerparent);

						this.add(this.inl);
						this.add(this.inr);
					}

					/** Sets the lims. */
					public void setLims() {
						float fl1;
						int i1;

						WindCard.this.spinmx = 2.75 * WindCard.this.vfsd / WindCard.this.vconv
								/ (WindCard.this.radius / WindCard.this.lconv);
						WindCard.this.spinmn = -2.75 * WindCard.this.vfsd / WindCard.this.vconv
								/ (WindCard.this.radius / WindCard.this.lconv);
						if (WindCard.this.spin * 60.0 < WindCard.this.spinmn) {
							WindCard.this.spin = WindCard.this.spinmn / 60.0;
							fl1 = (float) (WindCard.this.spin * 60.0);
							this.inl.f1.setText(String.valueOf(fl1));
						}
						if (WindCard.this.spin * 60.0 > WindCard.this.spinmx) {
							WindCard.this.spin = WindCard.this.spinmx / 60.0;
							fl1 = (float) (WindCard.this.spin * 60.0);
							this.inl.f1.setText(String.valueOf(fl1));
						}
						i1 = (int) ((60 * WindCard.this.spin - WindCard.this.spinmn)
								/ (WindCard.this.spinmx - WindCard.this.spinmn) * 1000.);
						this.inr.s1.setValue(i1);
					}
				} // Cyl

				/** The Class Flt. */
				class Flt extends Panel {

					/** The Class Lwr. */
					class Lwr extends Panel {

						/** The Constant serialVersionUID. */
						private static final long serialVersionUID = 1L;

						/** The end test. */
						private Button resetBtn;

						/** The endit. */
						private Button returnBtn;

						/** The speed. */
						private Button speed;

						/** The angle. */
						private Button angle;

						/** The pressure. */
						private Button pressure;

						/** The begin. */
						private Button begin;

						/** The take data. */
						private Button takeData;

						/** The end test. */
						private Button endTest;

						/** The outerparent. */
						// private Sys outerparent;

						/** The endact. */
						private ActionListener spdact;

						/** The angact. */
						private ActionListener angact;

						/** The presact. */
						private ActionListener presact;

						/** The begact. */
						private ActionListener begact;

						/** The takact. */
						private ActionListener takact;

						/** The endact. */
						private ActionListener endact;

						/** The select test. */
						private Label testNumber;

						/** The point number. */
						private Label pointNumber;

						/** The select test. */
						private Label selectTest;

						/** The point number txt. */
						private TextField testNumberTxt;

						/** The point number txt. */
						private TextField pointNumberTxt;

						/** The test type. */
						private int testSet;

						/** The test type. */
						private int testType;

						/** The untch. */
						private Choice untch;

						/**
						 * Instantiates a new lwr.
						 *
						 * @param target
						 *            the target
						 */
						Lwr(Sys target) {

							// this.outerparent = target;
							this.setLayout(new GridLayout(5, 4, 2, 5));

							this.spdact = e -> {
								Lwr.this.testType = 1;

								Lwr.this.speed.setBackground(Color.yellow);
								Lwr.this.speed.setForeground(Color.black);
								Lwr.this.angle.setBackground(Color.blue);
								Lwr.this.angle.setForeground(Color.white);
								Lwr.this.pressure.setBackground(Color.blue);
								Lwr.this.pressure.setForeground(Color.white);

								Flt.this.upr.inr.s1.setVisible(true);
								Flt.this.upr.inr.s2.setVisible(false);
								Flt.this.upr.inr.s3.setVisible(false);

								Flt.this.upr.inl.f1.setEditable(true);
								Flt.this.upr.inl.f2.setEditable(false);
								Flt.this.upr.inl.f3.setEditable(false);
							};

							this.angact = e -> {
								Lwr.this.testType = 2;

								Lwr.this.speed.setBackground(Color.blue);
								Lwr.this.speed.setForeground(Color.white);
								Lwr.this.angle.setBackground(Color.yellow);
								Lwr.this.angle.setForeground(Color.black);
								Lwr.this.pressure.setBackground(Color.blue);
								Lwr.this.pressure.setForeground(Color.white);

								Flt.this.upr.inr.s1.setVisible(false);
								Flt.this.upr.inr.s2.setVisible(false);
								Flt.this.upr.inr.s3.setVisible(true);

								Flt.this.upr.inl.f1.setEditable(false);
								Flt.this.upr.inl.f2.setEditable(false);
								Flt.this.upr.inl.f3.setEditable(true);
							};
							this.presact = e -> {
								Lwr.this.testType = 3;

								Lwr.this.speed.setBackground(Color.blue);
								Lwr.this.speed.setForeground(Color.white);
								Lwr.this.angle.setBackground(Color.blue);
								Lwr.this.angle.setForeground(Color.white);
								Lwr.this.pressure.setBackground(Color.yellow);
								Lwr.this.pressure.setForeground(Color.black);

								Flt.this.upr.inr.s1.setVisible(false);
								Flt.this.upr.inr.s2.setVisible(true);
								Flt.this.upr.inr.s3.setVisible(false);

								Flt.this.upr.inl.f1.setEditable(false);
								Flt.this.upr.inl.f2.setEditable(true);
								Flt.this.upr.inl.f3.setEditable(false);
							};

							this.begact = e -> {
								Lwr.this.begin.setBackground(Color.green);
								Lwr.this.begin.setForeground(Color.black);
								Lwr.this.testSet = Integer.parseInt(Lwr.this.testNumberTxt.getText());
								Lwr.this.testSet = Lwr.this.testSet + 1;
								if (Lwr.this.testSet > 20) {
									Lwr.this.testNumberTxt.setText("Max # of Tests");
								}
								final String test = Integer.toString(Lwr.this.testSet);
								Lwr.this.testNumberTxt.setText(test);

								WindCard.this.pointSet = 0;
								Lwr.this.pointNumberTxt.setText("0");

								Lwr.this.speed.removeActionListener(Lwr.this.spdact);
								Lwr.this.angle.removeActionListener(Lwr.this.angact);
								Lwr.this.pressure.removeActionListener(Lwr.this.presact);
								Lwr.this.takeData.addActionListener(Lwr.this.takact);
							};

							this.takact = e -> {
								final int modelNumber = Integer.parseInt(WindCard.this.conw.dwn.mod.getText());
								if (WindCard.this.pointSet < 40) {
									Sys.this.datnum = Sys.this.datnum + 1;
									Sys.bsav[1][Sys.this.datnum] = WindCard.this.camval;
									Sys.bsav[2][Sys.this.datnum] = WindCard.this.thkval;
									Sys.bsav[3][Sys.this.datnum] = WindCard.this.chord;
									Sys.bsav[4][Sys.this.datnum] = WindCard.this.span;
									Sys.bsav[5][Sys.this.datnum] = WindCard.this.q0;
									Sys.bsav[6][Sys.this.datnum] = WindCard.this.lift;
									Sys.bsav[7][Sys.this.datnum] = WindCard.this.vfsd;
									Sys.bsav[8][Sys.this.datnum] = WindCard.this.alfval;
									Sys.bsav[9][Sys.this.datnum] = WindCard.this.psin;
									Sys.bsavi[1][Sys.this.datnum] = Lwr.this.testSet;
									Sys.bsavi[2][Sys.this.datnum] = WindCard.this.pointSet;
									Sys.bsavi[3][Sys.this.datnum] = modelNumber;
									Sys.bsavi[4][Sys.this.datnum] = WindCard.this.lunits;
									Sys.bsavi[5][Sys.this.datnum] = Lwr.this.testType;
									Sys.bsavi[6][Sys.this.datnum] = WindCard.this.foiltype;

									WindCard.this.pointSet = WindCard.this.pointSet + 1;
									final String point = Integer.toString(WindCard.this.pointSet);
									Lwr.this.pointNumberTxt.setText(point);
								} else {
									Lwr.this.pointNumberTxt.setText("Max # of Points");
								}
							};

							this.endact = e -> {
								Lwr.this.begin.setBackground(Color.black);
								Lwr.this.begin.setForeground(Color.white);

								Lwr.this.speed.setBackground(Color.blue);
								Lwr.this.speed.setForeground(Color.white);

								Lwr.this.angle.setBackground(Color.blue);
								Lwr.this.angle.setForeground(Color.white);

								Lwr.this.pressure.setBackground(Color.blue);
								Lwr.this.pressure.setForeground(Color.white);

								Lwr.this.speed.addActionListener(Lwr.this.spdact);
								Lwr.this.angle.addActionListener(Lwr.this.angact);
								Lwr.this.pressure.addActionListener(Lwr.this.presact);
								Lwr.this.takeData.removeActionListener(Lwr.this.takact);

								Flt.this.upr.inr.s1.setVisible(true);
								Flt.this.upr.inr.s2.setVisible(true);
								Flt.this.upr.inr.s3.setVisible(true);
								Flt.this.upr.inl.f1.setEditable(true);
								Flt.this.upr.inl.f2.setEditable(true);
								Flt.this.upr.inl.f3.setEditable(true);
							};

							this.resetBtn = new Button(Sys.RESET);
							this.resetBtn.setBackground(Color.magenta);
							this.resetBtn.setForeground(Color.white);

							this.returnBtn = new Button(Sys.RETURN);
							this.returnBtn.setBackground(Color.red);
							this.returnBtn.setForeground(Color.white);

							this.untch = new Choice();
							this.untch.addItem("Imperial");
							this.untch.addItem("Metric");
							this.untch.setBackground(Color.white);
							this.untch.setForeground(Color.red);
							this.untch.select(0);

							this.testNumber = new Label("Test #: ", Label.RIGHT);
							this.pointNumber = new Label("Point #: ", Label.RIGHT);
							this.selectTest = new Label("Select Test: ", Label.CENTER);

							this.testNumberTxt = new TextField("0");
							this.testNumberTxt.setEditable(false);
							this.testNumberTxt.setBackground(Color.black);
							this.testNumberTxt.setForeground(Color.yellow);

							this.pointNumberTxt = new TextField("0");
							this.pointNumberTxt.setEditable(false);
							this.pointNumberTxt.setBackground(Color.black);
							this.pointNumberTxt.setForeground(Color.yellow);

							this.speed = new Button("Speed");
							this.speed.setBackground(Color.blue);
							this.speed.setForeground(Color.white);
							this.speed.addActionListener(this.spdact);

							this.angle = new Button("Angle of Attack");
							this.angle.setBackground(Color.blue);
							this.angle.setForeground(Color.white);
							this.angle.addActionListener(this.angact);

							this.pressure = new Button(Sys.PRESSURE);
							this.pressure.setBackground(Color.blue);
							this.pressure.setForeground(Color.white);
							this.pressure.addActionListener(this.presact);

							this.begin = new Button("Begin Test");
							this.begin.setBackground(Color.black);
							this.begin.setForeground(Color.white);
							this.begin.addActionListener(this.begact);

							this.takeData = new Button("Take Data Point");
							this.takeData.setBackground(Color.blue);
							this.takeData.setForeground(Color.white);

							this.endTest = new Button("End Test");
							this.endTest.setBackground(Color.black);
							this.endTest.setForeground(Color.white);
							this.endTest.addActionListener(this.endact);
							this.add(this.selectTest);
							this.add(this.speed);
							this.add(this.angle);
							this.add(this.pressure);

							this.add(new Label("", Label.CENTER));
							this.add(this.begin);
							this.add(this.takeData);
							this.add(this.endTest);

							this.add(this.testNumber);
							this.add(this.testNumberTxt);
							this.add(this.pointNumber);
							this.add(this.pointNumberTxt);

							this.add(new Label("", Label.CENTER));
							this.add(new Label("", Label.CENTER));
							this.add(new Label("", Label.CENTER));
							this.add(new Label("", Label.CENTER));

							this.add(this.returnBtn);
							this.add(this.resetBtn);
							this.add(new Label("Units:", Label.RIGHT));
							this.add(this.untch);
						}

						/**
						 * (non-Javadoc).
						 *
						 * @param evt
						 *            the evt
						 * @param arg
						 *            the arg
						 * @return true, if successful
						 * @see java.awt.Component#action(java.awt.Event, java.lang.Object)
						 */
						@Override
						public boolean action(Event evt, Object arg) {
							if (evt.target instanceof Choice) {
								this.handleCho(evt);
								return true;
							}
							if (evt.target instanceof Button) {
								this.handleRefs(evt, arg);
								return true;
							} else {
								return false;
							}
						} // handler

						/**
						 * Handle cho.
						 *
						 * @param evt
						 *            the evt
						 */
						public void handleCho(Event evt) {
							WindCard.this.lunits = this.untch.getSelectedIndex();
							WindCard.this.setUnits();
							WindCard.this.loadInput();

							WindCard.this.computeFlow();
						} // handle choice

						/**
						 * Handle refs.
						 *
						 * @param evt
						 *            the evt
						 * @param arg
						 *            the arg
						 */
						public void handleRefs(Event evt, Object arg) {
							final String label = (String) arg;
							if (label.equals(Sys.RETURN)) {
								Sys.this.mainPannel.mainMenu.f2.setText("Completed");
								Sys.this.mainPannel.mainMenu.f2.setForeground(Color.red);
								Sys.this.mainPannel.mainMenu.showFirstCard();
							}
							if (label.equals(Sys.RESET)) {
								WindCard.this.inw.flt.upr.inl.loadModelsBtn.setBackground(Color.blue);
								WindCard.this.inw.flt.upr.inl.loadModelsBtn.setForeground(Color.white);

								this.begin.setBackground(Color.black);
								this.begin.setForeground(Color.white);

								this.speed.setBackground(Color.blue);
								this.speed.setForeground(Color.white);

								this.angle.setBackground(Color.blue);
								this.angle.setForeground(Color.white);

								this.pressure.setBackground(Color.blue);
								this.pressure.setForeground(Color.white);

								this.testNumberTxt.setText("0");
								this.pointNumberTxt.setText("0");

								Flt.this.upr.inr.s1.setVisible(true);
								Flt.this.upr.inr.s2.setVisible(true);
								Flt.this.upr.inr.s3.setVisible(true);

								Flt.this.upr.inl.f1.setEditable(true);
								Flt.this.upr.inl.f2.setEditable(true);
								Flt.this.upr.inl.f3.setEditable(true);

								WindCard.this.solvew.setDefaults();
								WindCard.this.layin.show(WindCard.this.inw, MainPanel.FIRST_CARD);
								this.untch.select(WindCard.this.lunits);
								// **** the lunits check MUST come first
								WindCard.this.setUnits();
								WindCard.this.layplt.show(WindCard.this.ouw.grf.l, MainPanel.FIRST_CARD);
								WindCard.this.layout.show(WindCard.this.ouw, MainPanel.FIRST_CARD);
								WindCard.this.outopt = 0;

								WindCard.this.loadInput();
							}
						}
					} // Lwr

					/** The Class Upr. */
					class Upr extends Panel {

						/** The Class Inl. */
						class Inl extends Panel {

							/** The Constant serialVersionUID. */
							private static final long serialVersionUID = 1L;

							/** The bprto. */
							// private Button brbt;
							private Button loadModelsBtn;

							/** The f 4. */
							private TextField f1;

							/** The f 2. */
							private TextField f2;

							/** The f 3. */
							private TextField f3;

							/** The f 4. */
							private TextField f4;

							/** The l 5. */
							private Label l1;

							/** The l 2. */
							private Label l2;

							/** The l 3. */
							private Label l3;
							// private Label l4;
							// private Label l5;

							/**
							 * The outerparent.
							 *
							 * @param target
							 *            the target
							 */
							// private Sys outerparent;

							/**
							 * Instantiates a new inl.
							 *
							 * @param target
							 *            the target
							 */
							Inl(Sys target) {

								// this.outerparent = target;
								this.setLayout(new GridLayout(6, 2, 2, 5));

								this.l1 = new Label("Speed-mph", Label.CENTER);
								this.f1 = new TextField(Sys.ZERO_ZERO, 5);

								this.l2 = new Label("Pressure- psf", Label.CENTER);
								this.f2 = new TextField("2116", 5);

								this.l3 = new Label("Angle-deg", Label.CENTER);
								this.f3 = new TextField(Sys.ZERO_ZERO, 5);

								// this.l4 = new Label("Temperature - R", Label.CENTER);
								this.f4 = new TextField("518.6", 5);

								this.loadModelsBtn = new Button("Load Models");
								this.loadModelsBtn.setBackground(Color.blue);
								this.loadModelsBtn.setForeground(Color.white);

								this.add(this.l1);
								this.add(this.f1);

								this.add(this.l3);
								this.add(this.f3);

								this.add(this.l2);
								this.add(this.f2);

								// add(l4) ;
								// add(f4) ;

								this.add(new Label("", Label.CENTER));
								this.add(new Label("", Label.CENTER));

								this.add(new Label("", Label.CENTER));
								this.add(this.loadModelsBtn);

								this.add(new Label("", Label.CENTER));
								this.add(new Label("", Label.CENTER));
							}

							/**
							 * (non-Javadoc).
							 *
							 * @param evt
							 *            the evt
							 * @param arg
							 *            the arg
							 * @return true, if successful
							 * @see java.awt.Component#action(java.awt.Event, java.lang.Object)
							 */
							@Override
							public boolean action(Event evt, Object arg) {
								if (evt.target instanceof TextField) {
									this.handleText(evt);
									return true;
								}
								if (evt.target instanceof Button) {
									this.handleBut(evt, arg);
									return true;
								} else {
									return false;
								}
							} // handler

							/**
							 * Handle but.
							 *
							 * @param evt
							 *            the evt
							 * @param arg
							 *            the arg
							 */
							public void handleBut(Event evt, Object arg) {
								final String label = (String) arg;
								String labmod, labrd;
								int i;

								labmod = "  models";
								labrd = "Models Loaded -";

								if (label.equals("Load Models")) {
									this.loadModelsBtn.setBackground(Color.yellow);
									this.loadModelsBtn.setForeground(Color.black);

									for (i = 1; i <= Sys.this.counter - 1; ++i) {
										WindCard.this.dcam[i] = Sys.acam[i];
										WindCard.this.dthk[i] = Sys.athk[i];
										WindCard.this.dspan[i] = Sys.aspan[i];
										WindCard.this.dchrd[i] = Sys.achrd[i];
										WindCard.this.nummod = Sys.acount[i];
										WindCard.this.dftp[i] = Sys.aftp[i];
										WindCard.this.dlunits[i] = Sys.alunits[i];
									}

									Sys.MainPanel.WindCard.Inw.Flt.Upr.this.inr.modf
											.setText(labrd + WindCard.this.nummod + labmod);
								}
							} // end handler

							/**
							 * Handle text.
							 *
							 * @param evt
							 *            the evt
							 */
							public void handleText(Event evt) {
								Double V1, V2, V3, V4;
								double v1, v2, v3;
								float fl1;
								int i1, i2, i3;
								V1 = Double.valueOf(this.f1.getText());
								v1 = V1.doubleValue();
								V2 = Double.valueOf(this.f2.getText());
								v2 = V2.doubleValue();
								V3 = Double.valueOf(this.f3.getText());
								v3 = V3.doubleValue();
								V4 = Double.valueOf(this.f4.getText());
								V4.doubleValue();

								WindCard.this.vfsd = v1;
								if (v1 < WindCard.this.vmn) {
									WindCard.this.vfsd = v1 = WindCard.this.vmn;
									fl1 = (float) v1;
									this.f1.setText(String.valueOf(fl1));
								}
								if (v1 > WindCard.this.vmx) {
									WindCard.this.vfsd = v1 = WindCard.this.vmx;
									fl1 = (float) v1;
									this.f1.setText(String.valueOf(fl1));
								}

								WindCard.this.psin = v2;
								if (WindCard.this.psin < WindCard.this.psmn) {
									WindCard.this.psin = v2 = WindCard.this.psmn;
									fl1 = (float) v2;
									this.f2.setText(String.valueOf(fl1));
								}
								if (WindCard.this.psin > WindCard.this.psmx) {
									WindCard.this.psin = v2 = WindCard.this.psmx;
									fl1 = (float) v2;
									this.f2.setText(String.valueOf(fl1));
								}

								WindCard.this.alfval = v3;
								if (v3 < WindCard.this.angmn) {
									WindCard.this.alfval = v3 = WindCard.this.angmn;
									fl1 = (float) v3;
									this.f3.setText(String.valueOf(fl1));
								}
								if (v3 > WindCard.this.angmx) {
									WindCard.this.alfval = v3 = WindCard.this.angmx;
									fl1 = (float) v3;
									this.f3.setText(String.valueOf(fl1));
								}
								/*
								 * tsin = v4 ; if(tsin < tsmn) { tsin = v4 = tsmn ; fl1 = (float) v4 ;
								 * f4.setText(String.valueOf(fl1)) ; } if(tsin > tsmx) { tsin = v4 = tsmx ; fl1
								 * = (float) v4 ; f4.setText(String.valueOf(fl1)) ; }
								 */

								i1 = (int) ((v1 - WindCard.this.vmn) / (WindCard.this.vmx - WindCard.this.vmn) * 1000.);
								i2 = (int) ((v2 - WindCard.this.psmn) / (WindCard.this.psmx - WindCard.this.psmn)
										* 1000.);
								i3 = (int) ((v3 - WindCard.this.angmn) / (WindCard.this.angmx - WindCard.this.angmn)
										* 1000.);
								// i4 = (int) (((v4 - tsmn)/(tsmx-tsmn))*1000.) ;

								Sys.MainPanel.WindCard.Inw.Flt.Upr.this.inr.s1.setValue(i1);
								Sys.MainPanel.WindCard.Inw.Flt.Upr.this.inr.s2.setValue(i2);
								Sys.MainPanel.WindCard.Inw.Flt.Upr.this.inr.s3.setValue(i3);
								// inr.s4.setValue(i4) ;

								WindCard.this.computeFlow();
							} // Text Handler
						} // Inl

						/** The Class Inr. */
						class Inr extends Panel {

							/** The Constant serialVersionUID. */
							private static final long serialVersionUID = 1L;

							/** The modp. */
							private TextField modf;

							/** The modd. */
							private TextField modd;

							/** The modp. */
							private TextField modp;

							/** The outerparent. */
							// private Sys outerparent;

							/** The s 4. */
							private Scrollbar s1;

							/** The s 2. */
							private Scrollbar s2;

							/** The s 3. */
							private Scrollbar s3;

							/** The s 4. */
							private Scrollbar s4;

							/** The scroller. */
							private AdjustmentListener scroller;

							/**
							 * Instantiates a new inr.
							 *
							 * @param target
							 *            the target
							 */
							Inr(Sys target) {
								int i1, i2, i3, i4;

								// this.outerparent = target;
								this.setLayout(new GridLayout(6, 1, 2, 5));

								this.scroller = e -> {
									int ii1, ii2, ii3;
									double v1, v2, v3;
									float fl1, fl2, fl3;
									// Input for computations
									ii1 = Inr.this.s1.getValue();
									ii2 = Inr.this.s2.getValue();
									ii3 = Inr.this.s3.getValue();
									Inr.this.s4.getValue();

									WindCard.this.vfsd = v1 = ii1 * (WindCard.this.vmx - WindCard.this.vmn) / 1000.
											+ WindCard.this.vmn;
									WindCard.this.psin = v2 = ii2 * (WindCard.this.psmx - WindCard.this.psmn) / 1000.
											+ WindCard.this.psmn;
									WindCard.this.alfval = v3 = ii3 * (WindCard.this.angmx - WindCard.this.angmn)
											/ 1000. + WindCard.this.angmn;
									// tsin = v4 = ii4 * (tsmx - tsmn)/ 1000. + tsmn ;

									fl1 = (float) v1;
									fl2 = (float) v2;
									fl3 = (float) v3;
									// fl4 = (float) v4 ;

									Sys.MainPanel.WindCard.Inw.Flt.Upr.this.inl.f1.setText(String.valueOf(fl1));
									Sys.MainPanel.WindCard.Inw.Flt.Upr.this.inl.f2
											.setText(String.valueOf(Sys.filter3(fl2)));
									Sys.MainPanel.WindCard.Inw.Flt.Upr.this.inl.f3.setText(String.valueOf(fl3));
									// inl.f4.setText(String.valueOf(filter0(fl4))) ;

									WindCard.this.computeFlow();
								};

								i1 = (int) ((0.0 - WindCard.this.vmn) / (WindCard.this.vmx - WindCard.this.vmn)
										* 1000.);
								i2 = (int) ((2116.217 - WindCard.this.psmn) / (WindCard.this.psmax - WindCard.this.psmn)
										* 1000.);
								i3 = (int) ((WindCard.this.alfval - WindCard.this.angmn)
										/ (WindCard.this.angmx - WindCard.this.angmn) * 1000.);
								i4 = (int) ((518.6 - WindCard.this.tsmn) / (WindCard.this.tsmx - WindCard.this.tsmn)
										* 1000.);

								this.s1 = new Scrollbar(Scrollbar.HORIZONTAL, i1, 10, 0, 1000);
								this.s1.addAdjustmentListener(this.scroller);
								this.s2 = new Scrollbar(Scrollbar.HORIZONTAL, i2, 10, 0, 1000);
								this.s2.addAdjustmentListener(this.scroller);
								this.s3 = new Scrollbar(Scrollbar.HORIZONTAL, i3, 10, 0, 1000);
								this.s3.addAdjustmentListener(this.scroller);
								this.s4 = new Scrollbar(Scrollbar.HORIZONTAL, i4, 10, 0, 1000);
								this.s4.addAdjustmentListener(this.scroller);

								this.modf = new TextField("<-Load Models ");
								this.modf.setBackground(Color.white);
								this.modf.setForeground(Color.blue);

								this.modd = new TextField("Save to Data File ");
								this.modd.setBackground(Color.white);
								this.modd.setForeground(Color.blue);

								this.modp = new TextField("Print File ");
								this.modp.setBackground(Color.white);
								this.modp.setForeground(Color.blue);

								this.add(this.s1);
								this.add(this.s3);
								this.add(this.s2);
								// add(s4);
								this.add(new Label("", Label.CENTER));
								this.add(this.modf);
								this.add(new Label("", Label.CENTER));
							}
						} // Inr

						/** The Constant serialVersionUID. */
						private static final long serialVersionUID = 1L;

						/** The inl. */
						Inl inl;

						/** The inr. */
						Inr inr;

						/** The outerparent. */
						Sys outerparent;

						/**
						 * Instantiates a new upr.
						 *
						 * @param target
						 *            the target
						 */
						Upr(Sys target) {

							this.outerparent = target;
							this.setLayout(new GridLayout(1, 2, 5, 5));

							this.inl = new Inl(this.outerparent);
							this.inr = new Inr(this.outerparent);

							this.add(this.inl);
							this.add(this.inr);
						}
					} // Upr

					/** The Constant serialVersionUID. */
					private static final long serialVersionUID = 1L;

					/** The lwr. */
					Lwr lwr;

					/** The outerparent. */
					Sys outerparent;

					/** The upr. */
					Upr upr;

					/**
					 * Instantiates a new flt.
					 *
					 * @param target
					 *            the target
					 */
					Flt(Sys target) {

						this.outerparent = target;
						this.setLayout(new GridLayout(2, 1, 5, 5));

						this.upr = new Upr(this.outerparent);
						this.lwr = new Lwr(this.outerparent);

						this.add(this.upr);
						this.add(this.lwr);
					}
				} // Flt

				/** The Constant serialVersionUID. */
				private static final long serialVersionUID = 1L;

				/** The cyl. */
				Cyl cyl;

				/** The flt. */
				Flt flt;

				/** The outerparent. */
				Sys outerparent;

				/**
				 * Instantiates a new inw.
				 *
				 * @param target
				 *            the target
				 */
				Inw(Sys target) {
					this.outerparent = target;
					WindCard.this.layin = new CardLayout();
					this.setLayout(WindCard.this.layin);

					this.flt = new Flt(this.outerparent);
					this.cyl = new Cyl(this.outerparent);

					this.add(MainPanel.FIRST_CARD, this.flt);
					this.add(MainPanel.FOURTH_CARD, this.cyl);
				}
			} // In

			/** The Class Ouw. */
			class Ouw extends Panel {

				/** The Class Grf. */
				class Grf extends Panel {

					/** The Class L. */
					class L extends Panel {

						/** The Class C. */
						class C extends Panel {

							/** The Constant serialVersionUID. */
							private static final long serialVersionUID = 1L;

							/** The l 2. */
							private Label l2;

							/**
							 * The outerparent.
							 *
							 * @param target
							 *            the target
							 */
							// private Sys outerparent;

							/**
							 * Instantiates a new c.
							 *
							 * @param target
							 *            the target
							 */
							C(Sys target) {
								// this.outerparent = target;
								this.setLayout(new GridLayout(1, 1, 5, 5));

								this.l2 = new Label(Sys.SPACE_STR, Label.RIGHT);

								this.add(this.l2);
							}
						} // cyl

						/** The Class F. */
						class F extends Panel {

							/** The Constant serialVersionUID. */
							private static final long serialVersionUID = 1L;

							/** The l 2. */
							private Label l2;

							/** The outerparent. */
							// private Sys outerparent;

							/** The pl 9. */
							private Button angleBtn;

							/** The pl 4. */
							private Button thicknessBtn;

							/** The pl 5. */
							private Button camberBtn;

							/** The pl 6. */
							private Button speedBtn;

							/** The pl 7. */
							private Button altitudeBtn;

							/** The pl 8. */
							private Button wingAreaBtn;

							/** The pl 9. */
							private Button densityBtn;

							/** The plout. */
							private Choice plout;

							/**
							 * Instantiates a new f.
							 *
							 * @param target
							 *            the target
							 */
							F(Sys target) {
								// this.outerparent = target;
								this.setLayout(new GridLayout(3, 4, 5, 5));

								this.l2 = new Label("Lift vs.", Label.RIGHT);
								this.l2.setForeground(Color.red);

								this.plout = new Choice();
								this.plout.addItem("Lift vs.");
								this.plout.addItem("Cl vs.");
								this.plout.setBackground(Color.white);
								this.plout.setForeground(Color.red);
								this.plout.select(0);

								this.angleBtn = new Button(Sys.ANGLE);
								this.angleBtn.setBackground(Color.white);
								this.angleBtn.setForeground(Color.red);
								
								this.thicknessBtn = new Button("Thickness");
								this.thicknessBtn.setBackground(Color.white);
								this.thicknessBtn.setForeground(Color.red);
								
								this.camberBtn = new Button(Sys.CAMBER);
								this.camberBtn.setBackground(Color.white);
								this.camberBtn.setForeground(Color.red);
								
								this.speedBtn = new Button("Speed");
								this.speedBtn.setBackground(Color.white);
								this.speedBtn.setForeground(Color.red);
								
								this.altitudeBtn = new Button(Sys.ALTITUDE);
								this.altitudeBtn.setBackground(Color.white);
								this.altitudeBtn.setForeground(Color.red);
								
								this.wingAreaBtn = new Button(Sys.WING_AREA);
								this.wingAreaBtn.setBackground(Color.white);
								this.wingAreaBtn.setForeground(Color.red);
								
								this.densityBtn = new Button(Sys.DENSITY);
								this.densityBtn.setBackground(Color.white);
								this.densityBtn.setForeground(Color.red);

								this.add(this.l2);
								this.add(this.speedBtn);
								this.add(this.altitudeBtn);
								this.add(this.densityBtn);

								this.add(new Label(Sys.SPACE_STR, Label.RIGHT));
								this.add(this.wingAreaBtn);
								this.add(new Label(Sys.SPACE_STR, Label.RIGHT));
								this.add(new Label(Sys.SPACE_STR, Label.RIGHT));

								this.add(this.plout);
								this.add(this.angleBtn);
								this.add(this.camberBtn);
								this.add(this.thicknessBtn);
							}

							/**
							 * (non-Javadoc).
							 *
							 * @param evt
							 *            the evt
							 * @param arg
							 *            the arg
							 * @return true, if successful
							 * @see java.awt.Component#action(java.awt.Event, java.lang.Object)
							 */
							@Override
							public boolean action(Event evt, Object arg) {
								if (evt.target instanceof Button) {
									final String label = (String) arg;
									WindCard.this.layout.show(WindCard.this.ouw, MainPanel.FIRST_CARD);
									WindCard.this.outopt = 0;
									//TODO Build Consts
									if (label.equals(Sys.ANGLE)) {
										WindCard.this.dispp = 2;
									}
									if (label.equals("Thickness")) {
										WindCard.this.dispp = 3;
									}
									if (label.equals(Sys.CAMBER)) {
										WindCard.this.dispp = 4;
									}
									if (label.equals("Speed")) {
										WindCard.this.dispp = 5;
									}
									if (label.equals(Sys.ALTITUDE)) {
										WindCard.this.dispp = 6;
									}
									if (label.equals(Sys.WING_AREA)) {
										WindCard.this.dispp = 7;
									}
									if (label.equals(Sys.DENSITY)) {
										WindCard.this.dispp = 8;
									}

									WindCard.this.computeFlow();
									return true;
								}
								if (evt.target instanceof Choice) {
									final String label = (String) arg;
									if (label.equals("Lift vs.")) {
										WindCard.this.dout = 0;
									}
									if (label.equals("Cl vs.")) {
										WindCard.this.dout = 1;
									}

									return true;
								} else {
									return false;
								}
							} // Handler
						} // foil

						/** The Constant serialVersionUID. */
						private static final long serialVersionUID = 1L;

						/** The c. */
						C c;

						/** The f. */
						F f;

						/** The outerparent. */
						Sys outerparent;

						/**
						 * Instantiates a new l.
						 *
						 * @param target
						 *            the target
						 */
						L(Sys target) {
							this.outerparent = target;
							WindCard.this.layplt = new CardLayout();
							this.setLayout(WindCard.this.layplt);

							this.f = new F(this.outerparent);
							this.c = new C(this.outerparent);

							this.add(MainPanel.FIRST_CARD, this.f);
							this.add(MainPanel.SECOND_CARD, this.c);
						}
					} // Lower

					/** The Class U. */
					class U extends Panel {

						/** The Constant serialVersionUID. */
						private static final long serialVersionUID = 1L;

						/** The l 2. */
						private Label l1;

						/** The l 2. */
						private Label l2;

						/** The outerparent. */
						// private Sys outerparent;

						/** The pl 3. */
						private Button pressureBtn;

						/** The pl 2. */
						private Button pl2;

						/** The pl 3. */
						private Button planeBtn;

						/**
						 * Instantiates a new u.
						 *
						 * @param target
						 *            the target
						 */
						U(Sys target) {
							// this.outerparent = target;
							this.setLayout(new GridLayout(3, 4, 5, 5));

							this.l1 = new Label("Surface", Label.RIGHT);
							this.l1.setForeground(Color.blue);

							this.l2 = new Label("Generation", Label.RIGHT);
							this.l2.setForeground(Color.black);

							this.pressureBtn = new Button(Sys.PRESSURE);
							this.pressureBtn.setBackground(Color.white);
							this.pressureBtn.setForeground(Color.blue);
							
							this.pl2 = new Button(Sys.VELOCITY);
							this.pl2.setBackground(Color.white);
							this.pl2.setForeground(Color.blue);
							
							this.planeBtn = new Button("Plane");
							this.planeBtn.setBackground(Color.white);
							this.planeBtn.setForeground(Color.black);

							this.add(new Label("Select ", Label.RIGHT));
							this.add(new Label(" Plot ", Label.LEFT));
							this.add(new Label(Sys.SPACE_STR, Label.RIGHT));
							this.add(new Label(Sys.SPACE_STR, Label.RIGHT));
							this.add(this.l1);
							this.add(this.pressureBtn);
							this.add(this.pl2);
							this.add(new Label(Sys.SPACE_STR, Label.RIGHT));

							this.add(this.l2);
							this.add(this.planeBtn);
							this.add(new Label(Sys.SPACE_STR, Label.RIGHT));
							this.add(new Label(Sys.SPACE_STR, Label.RIGHT));
						}

						/**
						 * (non-Javadoc).
						 *
						 * @param evt
						 *            the evt
						 * @param arg
						 *            the arg
						 * @return true, if successful
						 * @see java.awt.Component#action(java.awt.Event, java.lang.Object)
						 */
						@Override
						public boolean action(Event evt, Object arg) {
							if (evt.target instanceof Button) {
								final String label = (String) arg;
								WindCard.this.layout.show(WindCard.this.ouw, MainPanel.FIRST_CARD);
								WindCard.this.outopt = 0;
								if (label.equals(Sys.PRESSURE)) {
									WindCard.this.dispp = 0;
								}
								if (label.equals(Sys.VELOCITY)) {
									WindCard.this.dispp = 1;
								}
								if (label.equals("Plane")) {
									WindCard.this.dispp = 25;
								}
								WindCard.this.calcrange = 0;
								WindCard.this.computeFlow();
								return true;
							} else {
								return false;
							}
						} // Handler
					} // Upper

					/** The Constant serialVersionUID. */
					private static final long serialVersionUID = 1L;

					/** The l. */
					L l;

					/** The outerparent. */
					Sys outerparent;

					/** The u. */
					U u;

					/**
					 * Instantiates a new grf.
					 *
					 * @param target
					 *            the target
					 */
					Grf(Sys target) {
						this.outerparent = target;
						this.setLayout(new GridLayout(2, 1, 5, 5));

						this.u = new U(this.outerparent);
						this.l = new L(this.outerparent);

						this.add(this.u);
						this.add(this.l);
					}
				} // Grf

				/** The Class Plt. */
				class OuwPlotter extends Canvas implements Runnable {

					/** The Constant serialVersionUID. */
					private static final long serialVersionUID = 1L;

					/** The ancp. */
					// private Point locp;
					// private Point ancp;

					/** The outerparent. */
					// private Sys outerparent;

					/** The run 2. */
					private Thread run2;

					/**
					 * Instantiates a new plt.
					 *
					 * @param target
					 *            the target
					 */
					OuwPlotter(Sys target) {
						this.setBackground(Color.blue);
						this.run2 = null;
					}

					/**
					 * Gets the clplot.
					 *
					 * @param camb
					 *            the camb
					 * @param thic
					 *            the thic
					 * @param angl
					 *            the angl
					 * @return the clplot
					 */
					public double getClplot(double camb, double thic, double angl) {
						double beta, xc, yc, rc, gamc, lec, tec, lecm, tecm, crdc;
						double number;

						xc = 0.0;
						yc = camb / 2.0;
						rc = thic / 4.0 + Math.sqrt(thic * thic / 16.0 + yc * yc + 1.0);
						xc = 1.0 - Math.sqrt(rc * rc - yc * yc);
						beta = Math.asin(yc / rc) / WindCard.this.convdr; /* Kutta condition */
						gamc = 2.0 * rc * Math.sin((angl + beta) * WindCard.this.convdr);
						lec = xc - Math.sqrt(rc * rc - yc * yc);
						tec = xc + Math.sqrt(rc * rc - yc * yc);
						lecm = lec + 1.0 / lec;
						tecm = tec + 1.0 / tec;
						crdc = tecm - lecm;
						// stall model 1
						WindCard.this.stfact = 1.0;
						if (WindCard.this.anflag == 1) {
							if (angl > 10.0) {
								WindCard.this.stfact = .5 + .1 * angl - .005 * angl * angl;
							}
							if (angl < -10.0) {
								WindCard.this.stfact = .5 - .1 * angl - .005 * angl * angl;
							}
						}

						number = WindCard.this.stfact * gamc * 4.0 * Sys.PI / crdc;

						if (WindCard.this.arcor == 1) { // correction for low aspect ratio
							number = number / (1.0 + number / (3.14159 * WindCard.this.aspr));
						}

						return number;
					}

					/**
					 * Handleb.
					 *
					 * @param x
					 *            the x
					 * @param y
					 *            the y
					 */
					public void handleb(int x, int y) {
						if (y <= 25) {
							/*
							 * if (x >= 5 && x <= 55) { rescale endy = 0.0 ; begy = 0.0 ; calcrange = 0 ;
							 * computeFlow() ; }
							 */
							if (x >= 52 && x <= 202) { // pressure plot
								WindCard.this.dispp = 0;
								WindCard.this.calcrange = 0;
								WindCard.this.computeFlow();
							}
							if (x >= 210 && x <= 360) { // velocity plot
								WindCard.this.dispp = 1;
								WindCard.this.calcrange = 0;
								WindCard.this.computeFlow();
							}
						}
						WindCard.this.ouw.plotter.repaint();
					}

					/** Load plot. */
					public void loadPlot() {
						double lftref, clref;
						double del, spd;
						double ppl, tpl, hpl, angl, thkpl, campl, clpl;
						int index, ic;

						WindCard.this.lines = 1;
						clref = this.getClplot(WindCard.this.camval, WindCard.this.thkval, WindCard.this.alfval);
						if (Math.abs(clref) <= .001) {
							clref = .001; /* protection */
						}
						lftref = clref * WindCard.this.q0 * WindCard.this.area / WindCard.this.lconv
								/ WindCard.this.lconv;
						// ******* attempt at constant re-scale
						WindCard.this.endy = 0.0;
						WindCard.this.begy = 0.0;
						WindCard.this.calcrange = 0;
						// ********

						// load up the view image
						for (ic = 0; ic <= WindCard.this.nlnc; ++ic) {
							for (index = 0; index <= WindCard.this.nptc; ++index) {
								if (WindCard.this.foiltype <= FOILTYPE_PLATE) {
									WindCard.this.xpl[ic][index] = WindCard.this.xm[ic][index];
									WindCard.this.ypl[ic][index] = WindCard.this.ym[ic][index];
								}
								if (WindCard.this.foiltype >= FOILTYPE_CYLINDER) {
									WindCard.this.xpl[ic][index] = WindCard.this.xg[ic][index];
									WindCard.this.ypl[ic][index] = WindCard.this.yg[ic][index];
								}
							}
						}
						// load up the generating plane
						if (WindCard.this.dispp == 25) {
							for (ic = 0; ic <= WindCard.this.nlnc; ++ic) {
								for (index = 0; index <= WindCard.this.nptc; ++index) {
									WindCard.this.xplg[ic][index] = WindCard.this.xgc[ic][index];
									WindCard.this.yplg[ic][index] = WindCard.this.ygc[ic][index];
								}
							}
						}
						// probe
						for (index = 0; index <= WindCard.this.nptc; ++index) {
							if (WindCard.this.foiltype <= FOILTYPE_PLATE) {
								WindCard.this.xpl[19][index] = WindCard.this.xm[19][index];
								WindCard.this.ypl[19][index] = WindCard.this.ym[19][index];
								WindCard.this.pxpl = WindCard.this.pxm;
								WindCard.this.pypl = WindCard.this.pym;
							}
							if (WindCard.this.foiltype >= FOILTYPE_CYLINDER) {
								WindCard.this.xpl[19][index] = WindCard.this.xg[19][index];
								WindCard.this.ypl[19][index] = WindCard.this.yg[19][index];
								WindCard.this.pxpl = WindCard.this.pxg;
								WindCard.this.pypl = WindCard.this.pyg;
							}
						}

						if (WindCard.this.dispp == 0) { // pressure variation
							WindCard.this.npt = WindCard.this.npt2;
							WindCard.this.ntr = 3;
							WindCard.this.nord = WindCard.this.nabs = 1;
							for (index = 1; index <= WindCard.this.npt; ++index) {
								if (WindCard.this.foiltype <= 3) {
									WindCard.this.pltx[0][index] = 100.
											* (WindCard.this.xpl[0][WindCard.this.npt2 - index + 1] / 4.0 + .5);
									WindCard.this.pltx[1][index] = 100.
											* (WindCard.this.xpl[0][WindCard.this.npt2 + index - 1] / 4.0 + .5);
									WindCard.this.pltx[2][index] = 100.
											* (WindCard.this.xpl[0][WindCard.this.npt2 + index - 1] / 4.0 + .5);
								}
								if (WindCard.this.foiltype >= FOILTYPE_CYLINDER) {
									WindCard.this.pltx[0][index] = 100.
											* (WindCard.this.xpl[0][WindCard.this.npt2 - index + 1]
													/ (2.0 * WindCard.this.radius / WindCard.this.lconv) + .5);
									WindCard.this.pltx[1][index] = 100.
											* (WindCard.this.xpl[0][WindCard.this.npt2 + index - 1]
													/ (2.0 * WindCard.this.radius / WindCard.this.lconv) + .5);
									WindCard.this.pltx[2][index] = 100.
											* (WindCard.this.xpl[0][WindCard.this.npt2 + index - 1]
													/ (2.0 * WindCard.this.radius / WindCard.this.lconv) + .5);
								}
								WindCard.this.plty[0][index] = WindCard.this.plp[WindCard.this.npt2 - index + 1];
								WindCard.this.plty[1][index] = WindCard.this.plp[WindCard.this.npt2 + index - 1];
								WindCard.this.plty[2][index] = WindCard.this.ps0 / 2116.217 * WindCard.this.pconv;
								// **** attempt to impose pstatic on surface plot for stalled foil
								if (index > 7) {
									if (WindCard.this.alfval > 10.0) {
										WindCard.this.plty[0][index] = WindCard.this.plty[2][index];
									}
									if (WindCard.this.alfval < -10.0) {
										WindCard.this.plty[1][index] = WindCard.this.plty[2][index];
									}
								}
								// *******
							}
							WindCard.this.begx = 0.0;
							WindCard.this.endx = 100.;
							WindCard.this.ntikx = 5;
							WindCard.this.ntiky = 5;
							// endy=1.02 * ps0/2116.217 * pconv ;
							// begy=.95 * ps0/2116.217 * pconv ;
							WindCard.this.laby = Sys.PRESS;
							if (WindCard.this.lunits == UNITS_ENGLISH) {
								WindCard.this.labyu = Sys.PSI;
							}
							if (WindCard.this.lunits == UNITS_METERIC) {
								WindCard.this.labyu = "k-Pa";
							}
							WindCard.this.labx = Sys.X2;
							if (WindCard.this.foiltype <= FOILTYPE_PLATE) {
								WindCard.this.labxu = Sys.CHORD3;
							}
							if (WindCard.this.foiltype >= 4) {
								WindCard.this.labxu = "% diameter";
							}
						}
						if (WindCard.this.dispp == 1) { // velocity variation
							WindCard.this.npt = WindCard.this.npt2;
							WindCard.this.ntr = 3;
							WindCard.this.nord = 2;
							WindCard.this.nabs = 1;
							for (index = 1; index <= WindCard.this.npt; ++index) {
								if (WindCard.this.foiltype <= FOILTYPE_PLATE) {
									WindCard.this.pltx[0][index] = 100.
											* (WindCard.this.xpl[0][WindCard.this.npt2 - index + 1] / 4.0 + .5);
									WindCard.this.pltx[1][index] = 100.
											* (WindCard.this.xpl[0][WindCard.this.npt2 + index - 1] / 4.0 + .5);
									WindCard.this.pltx[2][index] = 100.
											* (WindCard.this.xpl[0][WindCard.this.npt2 - index + 1] / 4.0 + .5);
								}
								if (WindCard.this.foiltype >= FOILTYPE_CYLINDER) {
									WindCard.this.pltx[0][index] = 100.
											* (WindCard.this.xpl[0][WindCard.this.npt2 - index + 1]
													/ (2.0 * WindCard.this.radius / WindCard.this.lconv) + .5);
									WindCard.this.pltx[1][index] = 100.
											* (WindCard.this.xpl[0][WindCard.this.npt2 + index - 1]
													/ (2.0 * WindCard.this.radius / WindCard.this.lconv) + .5);
									WindCard.this.pltx[2][index] = 100.
											* (WindCard.this.xpl[0][WindCard.this.npt2 + index - 1]
													/ (2.0 * WindCard.this.radius / WindCard.this.lconv) + .5);
								}
								WindCard.this.plty[0][index] = WindCard.this.plv[WindCard.this.npt2 - index + 1];
								WindCard.this.plty[1][index] = WindCard.this.plv[WindCard.this.npt2 + index - 1];
								WindCard.this.plty[2][index] = WindCard.this.vfsd;
								// **** attempt to impose free stream vel on surface plot for stalled foil
								if (index > 7) {
									if (WindCard.this.alfval > 10.0) {
										WindCard.this.plty[0][index] = WindCard.this.plty[2][index];
									}
									if (WindCard.this.alfval < -10.0) {
										WindCard.this.plty[1][index] = WindCard.this.plty[2][index];
									}
								}
								// *******
							}
							WindCard.this.begx = 0.0;
							WindCard.this.endx = 100.;
							WindCard.this.ntikx = 5;
							WindCard.this.ntiky = 6;
							// begy = 0.0 ;
							// endy = 500. ;
							WindCard.this.laby = "Vel";
							if (WindCard.this.lunits == UNITS_ENGLISH) {
								WindCard.this.labyu = Sys.MPH;
							}
							if (WindCard.this.lunits == UNITS_METERIC) {
								WindCard.this.labyu = "kmh";
							}
							WindCard.this.labx = Sys.X2;
							if (WindCard.this.foiltype <= FOILTYPE_PLATE) {
								WindCard.this.labxu = Sys.CHORD3;
							}
							if (WindCard.this.foiltype >= FOILTYPE_CYLINDER) {
								WindCard.this.labxu = "% diameter";
							}
						}
						if (WindCard.this.dispp == 2) { // lift versus angle
							WindCard.this.npt = 20;
							WindCard.this.ntr = 1;
							WindCard.this.nabs = 2;
							WindCard.this.nord = 3;
							WindCard.this.begx = -20.0;
							WindCard.this.endx = 20.0;
							WindCard.this.ntikx = 5;
							WindCard.this.labx = "Angle ";
							WindCard.this.labxu = "degrees";
							del = 40.0 / WindCard.this.npt;
							for (ic = 1; ic <= WindCard.this.npt; ++ic) {
								angl = -20.0 + (ic - 1) * del;
								clpl = this.getClplot(WindCard.this.camval, WindCard.this.thkval, angl);
								WindCard.this.pltx[0][ic] = angl;
								if (WindCard.this.dout == 0) {
									WindCard.this.plty[0][ic] = WindCard.this.fconv * lftref * clpl / clref;
								}
								if (WindCard.this.dout == 1) {
									WindCard.this.plty[0][ic] = 100. * clpl;
								}
							}
							WindCard.this.ntiky = 5;
							WindCard.this.pltx[1][0] = WindCard.this.alfval;
							if (WindCard.this.dout == 0) {
								WindCard.this.laby = Sys.LIFT2;
								if (WindCard.this.lunits == UNITS_ENGLISH) {
									WindCard.this.labyu = Sys.LBS;
								}
								if (WindCard.this.lunits == UNITS_METERIC) {
									WindCard.this.labyu = "N";
								}
								WindCard.this.plty[1][0] = lftref * WindCard.this.fconv;
							}
							if (WindCard.this.dout == 1) {
								WindCard.this.laby = "Cl";
								WindCard.this.labyu = Sys.X_100;
								WindCard.this.plty[1][0] = 100. * WindCard.this.clift;
							}
						}
						if (WindCard.this.dispp == 3) { // lift versus thickness
							WindCard.this.npt = 20;
							WindCard.this.ntr = 1;
							WindCard.this.nabs = 3;
							WindCard.this.nord = 3;
							WindCard.this.begx = 0.0;
							WindCard.this.endx = 25.0;
							WindCard.this.ntikx = 6;
							WindCard.this.labx = Sys.THICKNESS_SPACE;
							WindCard.this.labxu = Sys.CHORD3;
							del = 1.0 / WindCard.this.npt;
							for (ic = 1; ic <= WindCard.this.npt; ++ic) {
								thkpl = .05 + (ic - 1) * del;
								clpl = this.getClplot(WindCard.this.camval, thkpl, WindCard.this.alfval);
								WindCard.this.pltx[0][ic] = thkpl * 25.;
								if (WindCard.this.dout == 0) {
									WindCard.this.plty[0][ic] = WindCard.this.fconv * lftref * clpl / clref;
								}
								if (WindCard.this.dout == 1) {
									WindCard.this.plty[0][ic] = 100. * clpl;
								}
							}
							WindCard.this.ntiky = 5;
							WindCard.this.pltx[1][0] = WindCard.this.thkinpt;
							if (WindCard.this.dout == 0) {
								WindCard.this.laby = Sys.LIFT2;
								if (WindCard.this.lunits == UNITS_ENGLISH) {
									WindCard.this.labyu = Sys.LBS;
								}
								if (WindCard.this.lunits == UNITS_METERIC) {
									WindCard.this.labyu = "N";
								}
								WindCard.this.plty[1][0] = lftref * WindCard.this.fconv;
							}
							if (WindCard.this.dout == 1) {
								WindCard.this.laby = "Cl";
								WindCard.this.labyu = Sys.X_100;
								WindCard.this.plty[1][0] = 100. * WindCard.this.clift;
							}
						}
						if (WindCard.this.dispp == 4) { // lift versus camber
							WindCard.this.npt = 20;
							WindCard.this.ntr = 1;
							WindCard.this.nabs = 4;
							WindCard.this.nord = 3;
							WindCard.this.begx = -25.;
							WindCard.this.endx = 25.;
							WindCard.this.ntikx = 5;
							WindCard.this.labx = "Camber ";
							WindCard.this.labxu = Sys.CHORD3;
							del = 2.0 / WindCard.this.npt;
							for (ic = 1; ic <= WindCard.this.npt; ++ic) {
								campl = -1.0 + (ic - 1) * del;
								clpl = this.getClplot(campl, WindCard.this.thkval, WindCard.this.alfval);
								WindCard.this.pltx[0][ic] = campl * 25.0;
								if (WindCard.this.dout == 0) {
									WindCard.this.plty[0][ic] = WindCard.this.fconv * lftref * clpl / clref;
								}
								if (WindCard.this.dout == 1) {
									WindCard.this.plty[0][ic] = 100. * clpl;
								}
							}
							WindCard.this.ntiky = 5;
							WindCard.this.pltx[1][0] = WindCard.this.caminpt;
							if (WindCard.this.dout == 0) {
								WindCard.this.laby = Sys.LIFT2;
								if (WindCard.this.lunits == UNITS_ENGLISH) {
									WindCard.this.labyu = Sys.LBS;
								}
								if (WindCard.this.lunits == UNITS_METERIC) {
									WindCard.this.labyu = "N";
								}
								WindCard.this.plty[1][0] = lftref * WindCard.this.fconv;
							}
							if (WindCard.this.dout == 1) {
								WindCard.this.laby = "Cl";
								WindCard.this.labyu = Sys.X_100;
								WindCard.this.plty[1][0] = 100. * WindCard.this.clift;
							}
						}
						if (WindCard.this.dispp == 5) { // lift versus speed
							WindCard.this.npt = 20;
							WindCard.this.ntr = 1;
							WindCard.this.nabs = 5;
							WindCard.this.nord = 3;
							WindCard.this.begx = 0.0;
							WindCard.this.endx = 300.0;
							WindCard.this.ntikx = 7;
							WindCard.this.labx = "Speed ";
							if (WindCard.this.lunits == UNITS_ENGLISH) {
								WindCard.this.labxu = Sys.MPH;
							}
							if (WindCard.this.lunits == UNITS_METERIC) {
								WindCard.this.labxu = "kmh";
							}
							del = WindCard.this.vmax / WindCard.this.npt;
							for (ic = 1; ic <= WindCard.this.npt; ++ic) {
								spd = (ic - 1) * del;
								WindCard.this.pltx[0][ic] = spd;
								WindCard.this.plty[0][ic] = WindCard.this.fconv * lftref * spd * spd
										/ (WindCard.this.vfsd * WindCard.this.vfsd);
							}
							WindCard.this.ntiky = 5;
							WindCard.this.laby = Sys.LIFT2;
							WindCard.this.pltx[1][0] = WindCard.this.vfsd;
							WindCard.this.plty[1][0] = lftref * WindCard.this.fconv;
							if (WindCard.this.lunits == UNITS_ENGLISH) {
								WindCard.this.labyu = Sys.LBS;
							}
							if (WindCard.this.lunits == UNITS_METERIC) {
								WindCard.this.labyu = "N";
							}
						}
						if (WindCard.this.dispp == 6) { // lift versus altitude
							WindCard.this.npt = 20;
							WindCard.this.ntr = 1;
							WindCard.this.nabs = 6;
							WindCard.this.nord = 3;
							WindCard.this.begx = 0.0;
							WindCard.this.endx = 50.0;
							WindCard.this.ntikx = 6;
							if (WindCard.this.lunits == UNITS_ENGLISH) {
								WindCard.this.endx = 50.0;
							}
							if (WindCard.this.lunits == UNITS_METERIC) {
								WindCard.this.endx = 15.0;
							}
							WindCard.this.labx = Sys.ALTITUDE;
							if (WindCard.this.lunits == UNITS_ENGLISH) {
								WindCard.this.labxu = "k-ft";
							}
							if (WindCard.this.lunits == UNITS_METERIC) {
								WindCard.this.labxu = "km";
							}
							del = WindCard.this.altmax / WindCard.this.npt;
							for (ic = 1; ic <= WindCard.this.npt; ++ic) {
								hpl = (ic - 1) * del;
								WindCard.this.pltx[0][ic] = WindCard.this.lconv * hpl / 1000.;
								tpl = 518.6;
								ppl = 2116.217;
								if (WindCard.this.planet == 0) {
									if (hpl < 36152.) {
										tpl = 518.6 - 3.56 * hpl / 1000.;
										ppl = 2116.217 * Math.pow(tpl / 518.6, 5.256);
									} else {
										tpl = 389.98;
										ppl = 2116.217 * .236 * Math.exp((36000. - hpl) / (53.35 * tpl));
									}
									WindCard.this.plty[0][ic] = WindCard.this.fconv * lftref * ppl
											/ (tpl * 53.3 * 32.17) / WindCard.this.rho;
								}
								if (WindCard.this.planet == 1) {
									if (hpl <= 22960.) {
										tpl = 434.02 - .548 * hpl / 1000.;
										ppl = 14.62 * Math.pow(2.71828, -.00003 * hpl);
									}
									if (hpl > 22960.) {
										tpl = 449.36 - 1.217 * hpl / 1000.;
										ppl = 14.62 * Math.pow(2.71828, -.00003 * hpl);
									}
									WindCard.this.plty[0][ic] = WindCard.this.fconv * lftref * ppl / (tpl * 1149.)
											/ WindCard.this.rho;
								}
								if (WindCard.this.planet == 2) {
									WindCard.this.plty[0][ic] = WindCard.this.fconv * lftref;
								}
							}
							WindCard.this.ntiky = 5;
							WindCard.this.laby = Sys.LIFT2;
							WindCard.this.pltx[1][0] = WindCard.this.alt / 1000.;
							WindCard.this.plty[1][0] = lftref * WindCard.this.fconv;
							if (WindCard.this.lunits == UNITS_ENGLISH) {
								WindCard.this.labyu = Sys.LBS;
							}
							if (WindCard.this.lunits == UNITS_METERIC) {
								WindCard.this.labyu = "N";
							}
						}
						if (WindCard.this.dispp == 7) { // lift versus area
							WindCard.this.npt = 2;
							WindCard.this.ntr = 1;
							WindCard.this.nabs = 7;
							WindCard.this.nord = 3;
							WindCard.this.begx = 0.0;
							WindCard.this.ntikx = 6;
							WindCard.this.labx = "Area ";
							if (WindCard.this.lunits == UNITS_ENGLISH) {
								WindCard.this.labxu = "sq ft";
								WindCard.this.endx = 2000.0;
								WindCard.this.labyu = Sys.LBS;
								WindCard.this.pltx[0][1] = 0.0;
								WindCard.this.plty[0][1] = 0.0;
								WindCard.this.pltx[0][2] = 2000.;
								WindCard.this.plty[0][2] = WindCard.this.fconv * lftref * 2000. / WindCard.this.area;
							}
							if (WindCard.this.lunits == UNITS_METERIC) {
								WindCard.this.labxu = "sq m";
								WindCard.this.endx = 200.;
								WindCard.this.labyu = "N";
								WindCard.this.pltx[0][1] = 0.0;
								WindCard.this.plty[0][1] = 0.0;
								WindCard.this.pltx[0][2] = 200.;
								WindCard.this.plty[0][2] = WindCard.this.fconv * lftref * 200. / WindCard.this.area;
								;
							}

							WindCard.this.ntiky = 5;
							WindCard.this.laby = Sys.LIFT2;
							WindCard.this.pltx[1][0] = WindCard.this.area;
							WindCard.this.plty[1][0] = lftref * WindCard.this.fconv;
						}
						if (WindCard.this.dispp == 8) { // lift versus density
							WindCard.this.npt = 2;
							WindCard.this.ntr = 1;
							WindCard.this.nabs = 7;
							WindCard.this.nord = 3;
							WindCard.this.begx = 0.0;
							WindCard.this.ntikx = 6;
							WindCard.this.labx = "Density ";
							if (WindCard.this.planet == 0) {
								if (WindCard.this.lunits == UNITS_ENGLISH) {
									WindCard.this.labxu = "x 10,000 slug/cu ft";
									WindCard.this.endx = 25.0;
									WindCard.this.pltx[0][1] = 0.0;
									WindCard.this.plty[0][1] = 0.0;
									WindCard.this.pltx[0][2] = 23.7;
									WindCard.this.plty[0][2] = WindCard.this.fconv * lftref * 23.7
											/ (WindCard.this.rho * 10000.);
									WindCard.this.pltx[1][0] = WindCard.this.rho * 10000.;
								}
								if (WindCard.this.lunits == UNITS_METERIC) {
									WindCard.this.labxu = Sys.G_CU_M;
									WindCard.this.endx = 1250.;
									WindCard.this.pltx[0][1] = 0.0;
									WindCard.this.plty[0][1] = 0.0;
									WindCard.this.pltx[0][2] = 1226;
									WindCard.this.plty[0][2] = WindCard.this.fconv * lftref * 23.7
											/ (WindCard.this.rho * 10000.);
									WindCard.this.pltx[1][0] = WindCard.this.rho * 1000. * 515.4;
								}
							}
							if (WindCard.this.planet == 1) {
								if (WindCard.this.lunits == UNITS_ENGLISH) {
									WindCard.this.labxu = "x 100,000 slug/cu ft";
									WindCard.this.endx = 5.0;
									WindCard.this.pltx[0][1] = 0.0;
									WindCard.this.plty[0][1] = 0.0;
									WindCard.this.pltx[0][2] = 2.93;
									WindCard.this.plty[0][2] = WindCard.this.fconv * lftref * 2.93
											/ (WindCard.this.rho * 100000.);
									WindCard.this.pltx[1][0] = WindCard.this.rho * 100000.;
								}
								if (WindCard.this.lunits == UNITS_METERIC) {
									WindCard.this.labxu = Sys.G_CU_M;
									WindCard.this.endx = 15.;
									WindCard.this.pltx[0][1] = 0.0;
									WindCard.this.plty[0][1] = 0.0;
									WindCard.this.pltx[0][2] = 15.1;
									WindCard.this.plty[0][2] = WindCard.this.fconv * lftref * 2.93
											/ (WindCard.this.rho * 100000.);
									WindCard.this.pltx[1][0] = WindCard.this.rho * 1000. * 515.4;
								}
							}
							WindCard.this.ntiky = 5;
							WindCard.this.laby = Sys.LIFT2;
							WindCard.this.plty[1][0] = lftref * WindCard.this.fconv;
							if (WindCard.this.lunits == UNITS_ENGLISH) {
								WindCard.this.labyu = Sys.LBS;
							}
							if (WindCard.this.lunits == UNITS_METERIC) {
								WindCard.this.labyu = "N";
							}
						}

						if (WindCard.this.dispp >= 2 && WindCard.this.dispp < 6) { // determine y - range zero in middle
							if (WindCard.this.plty[0][WindCard.this.npt] >= WindCard.this.plty[0][1]) {
								WindCard.this.begy = 0.0;
								if (WindCard.this.plty[0][1] > WindCard.this.endy) {
									WindCard.this.endy = WindCard.this.plty[0][1];
								}
								if (WindCard.this.plty[0][WindCard.this.npt] > WindCard.this.endy) {
									WindCard.this.endy = WindCard.this.plty[0][WindCard.this.npt];
								}
								if (WindCard.this.endy <= 0.0) {
									WindCard.this.begy = WindCard.this.plty[0][1];
									WindCard.this.endy = WindCard.this.plty[0][WindCard.this.npt];
								}
							}
							if (WindCard.this.plty[0][WindCard.this.npt] < WindCard.this.plty[0][1]) {
								WindCard.this.endy = 0.0;
								if (WindCard.this.plty[0][1] < WindCard.this.begy) {
									WindCard.this.begy = WindCard.this.plty[0][1];
								}
								if (WindCard.this.plty[0][WindCard.this.npt] < WindCard.this.begy) {
									WindCard.this.begy = WindCard.this.plty[0][WindCard.this.npt];
								}
								if (WindCard.this.begy <= 0.0) {
									WindCard.this.begy = WindCard.this.plty[0][WindCard.this.npt];
									WindCard.this.endy = WindCard.this.plty[0][1];
								}
							}
						}
						if (WindCard.this.dispp >= 6 && WindCard.this.dispp <= 8) { // determine y - range
							if (WindCard.this.plty[0][WindCard.this.npt] >= WindCard.this.plty[0][1]) {
								WindCard.this.begy = WindCard.this.plty[0][1];
								WindCard.this.endy = WindCard.this.plty[0][WindCard.this.npt];
							}
							if (WindCard.this.plty[0][WindCard.this.npt] < WindCard.this.plty[0][1]) {
								WindCard.this.begy = WindCard.this.plty[0][WindCard.this.npt];
								WindCard.this.endy = WindCard.this.plty[0][1];
							}
						}
						if (WindCard.this.dispp >= 0 && WindCard.this.dispp <= 1) { // determine y - range
							if (WindCard.this.calcrange == 0) {
								WindCard.this.begy = WindCard.this.plty[0][1];
								WindCard.this.endy = WindCard.this.plty[0][1];
								for (index = 1; index <= WindCard.this.npt2; ++index) {
									if (WindCard.this.plty[0][index] < WindCard.this.begy) {
										WindCard.this.begy = WindCard.this.plty[0][index];
									}
									if (WindCard.this.plty[1][index] < WindCard.this.begy) {
										WindCard.this.begy = WindCard.this.plty[1][index];
									}
									if (WindCard.this.plty[0][index] > WindCard.this.endy) {
										WindCard.this.endy = WindCard.this.plty[0][index];
									}
									if (WindCard.this.plty[1][index] > WindCard.this.endy) {
										WindCard.this.endy = WindCard.this.plty[1][index];
									}
								}
								WindCard.this.calcrange = 1;
							}
						}
					}

					/**
					 * (non-Javadoc).
					 *
					 * @param evt
					 *            the evt
					 * @param x
					 *            the x
					 * @param y
					 *            the y
					 * @return true, if successful
					 * @see java.awt.Component#mouseUp(java.awt.Event, int, int)
					 */
					@Override
					public boolean mouseUp(Event evt, int x, int y) {
						this.handleb(x, y);
						return true;
					}

					/**
					 * (non-Javadoc).
					 *
					 * @param g
					 *            the g
					 * @see java.awt.Canvas#paint(java.awt.Graphics)
					 */
					@Override
					public void paint(Graphics g) {
						if (Sys.this.mainPannel.windCard.isVisible()) {
							int i, j, k;
							int index;
							int xlabel, ylabel, ind;
							final int exes[] = new int[8];
							final int whys[] = new int[8];
							double offx, scalex, offy, scaley;
							double incy, incx;
							double xl, yl;
							double liftab;
							if (WindCard.this.dispp <= 1) {
								Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.black);
								Sys.this.ouwPlotterImgBufGraphContext.fillRect(0, 0, Sys.OUW_PLOTTER_WIDTH,
										Sys.OUW_PLOTTER_HEIGHT);
								/*
								 * off4Gg.setColor(Color.white) ; off4Gg.fillRect(2,302,70,15) ;
								 * off4Gg.setColor(Color.red) ; off4Gg.drawString("Rescale",8,315) ;
								 */
								Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.lightGray);
								Sys.this.ouwPlotterImgBufGraphContext.fillRect(0, 0, 500, 30);
								Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.white);
								if (WindCard.this.dispp == 0) {
									Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.yellow);
								}
								Sys.this.ouwPlotterImgBufGraphContext.fillRect(52, 7, 150, 20);
								Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.black);
								Sys.this.ouwPlotterImgBufGraphContext.drawString("Surface Pressure", 58, 22);

								Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.white);
								if (WindCard.this.dispp == 1) {
									Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.yellow);
								}
								Sys.this.ouwPlotterImgBufGraphContext.fillRect(210, 7, 120, 20);
								Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.black);
								Sys.this.ouwPlotterImgBufGraphContext.drawString(Sys.VELOCITY, 258, 22);
							}
							if (WindCard.this.dispp > 1 && WindCard.this.dispp <= 15) {
								Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.blue);
								Sys.this.ouwPlotterImgBufGraphContext.fillRect(0, 0, 500, 500);
								/*
								 * off4Gg.setColor(Color.white) ; off4Gg.fillRect(2,302,70,15) ;
								 * off4Gg.setColor(Color.red) ; off4Gg.drawString("Rescale",8,315) ;
								 */
							}
							if (WindCard.this.dispp >= 20) {
								Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.black);
								Sys.this.ouwPlotterImgBufGraphContext.fillRect(0, 0, 500, 500);
							}

							if (WindCard.this.ntikx < 2) {
								WindCard.this.ntikx = 2; /* protection 13June96 */
							}
							if (WindCard.this.ntiky < 2) {
								WindCard.this.ntiky = 2;
							}
							offx = 0.0 - WindCard.this.begx;
							scalex = 6.0 / (WindCard.this.endx - WindCard.this.begx);
							incx = (WindCard.this.endx - WindCard.this.begx) / (WindCard.this.ntikx - 1);
							offy = 0.0 - WindCard.this.begy;
							scaley = 4.5 / (WindCard.this.endy - WindCard.this.begy);
							incy = (WindCard.this.endy - WindCard.this.begy) / (WindCard.this.ntiky - 1);

							if (WindCard.this.dispp <= 15) { /* draw a graph */
								/* draw axes */
								Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.white);
								exes[0] = (int) (WindCard.this.factp * 0.0) + WindCard.this.xtp;
								whys[0] = (int) (WindCard.this.factp * -4.5) + WindCard.this.ytp;
								exes[1] = (int) (WindCard.this.factp * 0.0) + WindCard.this.xtp;
								whys[1] = (int) (WindCard.this.factp * 0.0) + WindCard.this.ytp;
								exes[2] = (int) (WindCard.this.factp * 6.0) + WindCard.this.xtp;
								whys[2] = (int) (WindCard.this.factp * 0.0) + WindCard.this.ytp;
								Sys.this.ouwPlotterImgBufGraphContext.drawLine(exes[0], whys[0], exes[1], whys[1]);
								Sys.this.ouwPlotterImgBufGraphContext.drawLine(exes[1], whys[1], exes[2], whys[2]);

								xlabel = (int) -90.0 + WindCard.this.xtp; /* label y axis */
								ylabel = (int) (WindCard.this.factp * -1.5) + WindCard.this.ytp;
								Sys.this.ouwPlotterImgBufGraphContext.drawString(WindCard.this.laby, xlabel, ylabel);
								Sys.this.ouwPlotterImgBufGraphContext.drawString(WindCard.this.labyu, xlabel,
										ylabel + 10);
								/* add tick values */
								for (ind = 1; ind <= WindCard.this.ntiky; ++ind) {
									xlabel = (int) -50.0 + WindCard.this.xtp;
									yl = WindCard.this.begy + (ind - 1) * incy;
									ylabel = (int) (WindCard.this.factp * -scaley * (yl + offy)) + WindCard.this.ytp;
									if (WindCard.this.nord >= 2) {
										Sys.this.ouwPlotterImgBufGraphContext.drawString(String.valueOf((int) yl),
												xlabel, ylabel);
									} else {
										Sys.this.ouwPlotterImgBufGraphContext
												.drawString(String.valueOf(Sys.filter3(yl)), xlabel, ylabel);
									}
								}
								xlabel = (int) (WindCard.this.factp * 3.0) + WindCard.this.xtp; /* label x axis */
								ylabel = (int) 40.0 + WindCard.this.ytp;
								Sys.this.ouwPlotterImgBufGraphContext.drawString(WindCard.this.labx, xlabel,
										ylabel - 10);
								Sys.this.ouwPlotterImgBufGraphContext.drawString(WindCard.this.labxu, xlabel, ylabel);
								/* add tick values */
								for (ind = 1; ind <= WindCard.this.ntikx; ++ind) {
									ylabel = (int) 15. + WindCard.this.ytp;
									xl = WindCard.this.begx + (ind - 1) * incx;
									xlabel = (int) (WindCard.this.factp * (scalex * (xl + offx) - .05))
											+ WindCard.this.xtp;
									if (WindCard.this.nabs == 1) {
										Sys.this.ouwPlotterImgBufGraphContext.drawString(String.valueOf(xl), xlabel,
												ylabel);
									}
									if (WindCard.this.nabs > 1) {
										Sys.this.ouwPlotterImgBufGraphContext.drawString(String.valueOf((int) xl),
												xlabel, ylabel);
									}
								}

								if (WindCard.this.lines == 0) {
									for (i = 1; i <= WindCard.this.npt; ++i) {
										xlabel = (int) (WindCard.this.factp * scalex
												* (offx + WindCard.this.pltx[0][i])) + WindCard.this.xtp;
										ylabel = (int) (WindCard.this.factp * -scaley
												* (offy + WindCard.this.plty[0][i]) + 7.) + WindCard.this.ytp;
										Sys.this.ouwPlotterImgBufGraphContext.drawString("*", xlabel, ylabel);
									}
								} else {
									if (WindCard.this.dispp <= 1) {
										// if (anflag != 1 || (anflag == 1 && Math.abs(alfval) < 10.0)) {
										for (j = 0; j <= WindCard.this.ntr - 1; ++j) {
											k = 2 - j;
											if (k == 0) {
												Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.magenta);
												xlabel = (int) (WindCard.this.factp * 6.1) + WindCard.this.xtp;
												ylabel = (int) (WindCard.this.factp * -2.5) + WindCard.this.ytp;
												Sys.this.ouwPlotterImgBufGraphContext.drawString("Upper", xlabel,
														ylabel);
											}
											if (k == 1) {
												Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.yellow);
												xlabel = (int) (WindCard.this.factp * 6.1) + WindCard.this.xtp;
												ylabel = (int) (WindCard.this.factp * -1.5) + WindCard.this.ytp;
												Sys.this.ouwPlotterImgBufGraphContext.drawString("Lower", xlabel,
														ylabel);
											}
											if (k == 2) {
												Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.green);
												xlabel = (int) (WindCard.this.factp * 2.0) + WindCard.this.xtp;
												ylabel = (int) (WindCard.this.factp * -5.0) + WindCard.this.ytp;
												Sys.this.ouwPlotterImgBufGraphContext.drawString("Free Stream", xlabel,
														ylabel);
											}
											exes[1] = (int) (WindCard.this.factp * scalex
													* (offx + WindCard.this.pltx[k][1])) + WindCard.this.xtp;
											whys[1] = (int) (WindCard.this.factp * -scaley
													* (offy + WindCard.this.plty[k][1])) + WindCard.this.ytp;
											for (i = 1; i <= WindCard.this.npt; ++i) {
												exes[0] = exes[1];
												whys[0] = whys[1];
												exes[1] = (int) (WindCard.this.factp * scalex
														* (offx + WindCard.this.pltx[k][i])) + WindCard.this.xtp;
												whys[1] = (int) (WindCard.this.factp * -scaley
														* (offy + WindCard.this.plty[k][i])) + WindCard.this.ytp;
												Sys.this.ouwPlotterImgBufGraphContext.drawLine(exes[0], whys[0],
														exes[1], whys[1]);
											}
										}
										/*
										 * } if (anflag == 1 && Math.abs(alfval) > 10.0) { off4Gg.setColor(Color.yellow)
										 * ; xlabel = (int) (factp* 1.0) + xtp ; ylabel = (int) (factp* -2.0) + ytp ;
										 * off4Gg.drawString("Wing is Stalled",xlabel,ylabel) ;
										 *
										 * xlabel = (int) (factp* 1.0) + xtp ; ylabel = (int) (factp* -1.0) + ytp ;
										 * off4Gg.drawString("Plot not Available",xlabel,ylabel) ; }
										 */
									}
									if (WindCard.this.dispp > 1) {
										Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.white);
										exes[1] = (int) (WindCard.this.factp * scalex
												* (offx + WindCard.this.pltx[0][1])) + WindCard.this.xtp;
										whys[1] = (int) (WindCard.this.factp * -scaley
												* (offy + WindCard.this.plty[0][1])) + WindCard.this.ytp;
										for (i = 1; i <= WindCard.this.npt; ++i) {
											exes[0] = exes[1];
											whys[0] = whys[1];
											exes[1] = (int) (WindCard.this.factp * scalex
													* (offx + WindCard.this.pltx[0][i])) + WindCard.this.xtp;
											whys[1] = (int) (WindCard.this.factp * -scaley
													* (offy + WindCard.this.plty[0][i])) + WindCard.this.ytp;
											Sys.this.ouwPlotterImgBufGraphContext.drawLine(exes[0], whys[0], exes[1],
													whys[1]);
										}
									}
									xlabel = (int) (WindCard.this.factp * scalex * (offx + WindCard.this.pltx[1][0]))
											+ WindCard.this.xtp;
									ylabel = (int) (WindCard.this.factp * -scaley * (offy + WindCard.this.plty[1][0]))
											+ WindCard.this.ytp - 4;
									Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.red);
									Sys.this.ouwPlotterImgBufGraphContext.fillOval(xlabel, ylabel, 5, 5);
								}
							}
							if (WindCard.this.dispp == 20) { /* draw the lift gauge */
								Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.black);
								Sys.this.ouwPlotterImgBufGraphContext.fillRect(0, 100, 300, 30);
								// Thermometer gage
								Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.white);
								if (WindCard.this.lftout == 0) {
									Sys.this.ouwPlotterImgBufGraphContext.drawString("Lift =", 70, 75);
									if (WindCard.this.lunits == UNITS_ENGLISH) {
										Sys.this.ouwPlotterImgBufGraphContext.drawString("Pounds", 190, 75);
									}
									if (WindCard.this.lunits == UNITS_METERIC) {
										Sys.this.ouwPlotterImgBufGraphContext.drawString("Newtons", 190, 75);
									}
								}
								if (WindCard.this.lftout == 1) {
									Sys.this.ouwPlotterImgBufGraphContext.drawString(" Cl  =", 70, 75);
								}
								Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.yellow);
								for (index = 0; index <= 10; index++) {
									Sys.this.ouwPlotterImgBufGraphContext.drawLine(7 + index * 25, 100, 7 + index * 25,
											110);
									Sys.this.ouwPlotterImgBufGraphContext.drawString(String.valueOf(index),
											5 + index * 25, 125);
								}

								liftab = WindCard.this.lift;
								if (WindCard.this.lftout == 0) {
									if (Math.abs(WindCard.this.lift) <= 1.0) {
										liftab = WindCard.this.lift * 10.0;
										Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.cyan);
										Sys.this.ouwPlotterImgBufGraphContext.fillRect(0, 100,
												7 + (int) (25 * Math.abs(liftab)), 10);
										Sys.this.ouwPlotterImgBufGraphContext.drawString("-1", 180, 70);
									}
									if (Math.abs(WindCard.this.lift) > 1.0 && Math.abs(WindCard.this.lift) <= 10.0) {
										liftab = WindCard.this.lift;
										Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.yellow);
										Sys.this.ouwPlotterImgBufGraphContext.fillRect(0, 100,
												7 + (int) (25 * Math.abs(liftab)), 10);
										Sys.this.ouwPlotterImgBufGraphContext.drawString("0", 180, 70);
									}
									if (Math.abs(WindCard.this.lift) > 10.0 && Math.abs(WindCard.this.lift) <= 100.0) {
										liftab = WindCard.this.lift / 10.0;
										Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.green);
										Sys.this.ouwPlotterImgBufGraphContext.fillRect(0, 100,
												7 + (int) (25 * Math.abs(liftab)), 10);
										Sys.this.ouwPlotterImgBufGraphContext.drawString("1", 180, 70);
									}
									if (Math.abs(WindCard.this.lift) > 100.0
											&& Math.abs(WindCard.this.lift) <= 1000.0) {
										liftab = WindCard.this.lift / 100.0;
										Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.red);
										Sys.this.ouwPlotterImgBufGraphContext.fillRect(0, 100,
												7 + (int) (25 * Math.abs(liftab)), 10);
										Sys.this.ouwPlotterImgBufGraphContext.drawString("2", 180, 70);
									}
									if (Math.abs(WindCard.this.lift) > 1000.0
											&& Math.abs(WindCard.this.lift) <= 10000.0) {
										liftab = WindCard.this.lift / 1000.0;
										Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.magenta);
										Sys.this.ouwPlotterImgBufGraphContext.fillRect(0, 100,
												7 + (int) (25 * Math.abs(liftab)), 10);
										Sys.this.ouwPlotterImgBufGraphContext.drawString("3", 180, 70);
									}
									if (Math.abs(WindCard.this.lift) > 10000.0
											&& Math.abs(WindCard.this.lift) <= 100000.0) {
										liftab = WindCard.this.lift / 10000.0;
										Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.orange);
										Sys.this.ouwPlotterImgBufGraphContext.fillRect(0, 100,
												7 + (int) (25 * Math.abs(liftab)), 10);
										Sys.this.ouwPlotterImgBufGraphContext.drawString("4", 180, 70);
									}
									if (Math.abs(WindCard.this.lift) > 100000.0
											&& Math.abs(WindCard.this.lift) <= 1000000.0) {
										liftab = WindCard.this.lift / 100000.0;
										Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.white);
										Sys.this.ouwPlotterImgBufGraphContext.fillRect(0, 100,
												7 + (int) (25 * Math.abs(liftab)), 10);
										Sys.this.ouwPlotterImgBufGraphContext.drawString("5", 180, 70);
									}
									if (Math.abs(WindCard.this.lift) > 1000000.0) {
										liftab = WindCard.this.lift / 1000000.0;
										Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.white);
										Sys.this.ouwPlotterImgBufGraphContext.fillRect(0, 100,
												7 + (int) (25 * Math.abs(liftab)), 10);
										Sys.this.ouwPlotterImgBufGraphContext.drawString("6", 180, 70);
									}
								}
								if (WindCard.this.lftout == 1) {
									liftab = WindCard.this.clift;
									if (Math.abs(WindCard.this.clift) <= 1.0) {
										liftab = WindCard.this.clift * 10.0;
										Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.cyan);
										Sys.this.ouwPlotterImgBufGraphContext.fillRect(0, 100,
												7 + (int) (25 * Math.abs(liftab)), 10);
										Sys.this.ouwPlotterImgBufGraphContext.drawString("-1", 180, 70);
									}
									if (Math.abs(WindCard.this.clift) > 1.0 && Math.abs(WindCard.this.clift) <= 10.0) {
										liftab = WindCard.this.clift;
										Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.yellow);
										Sys.this.ouwPlotterImgBufGraphContext.fillRect(0, 100,
												7 + (int) (25 * Math.abs(liftab)), 10);
										Sys.this.ouwPlotterImgBufGraphContext.drawString("0", 180, 70);
									}
								}

								Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.white);
								Sys.this.ouwPlotterImgBufGraphContext.drawString(String.valueOf(Sys.filter3(liftab)),
										110, 75);
								Sys.this.ouwPlotterImgBufGraphContext.drawString(" X 10 ", 150, 75);
							}

							if (WindCard.this.dispp == 25) { /* draw the generating cylinder */
								Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.yellow);
								for (j = 1; j <= WindCard.this.nln2 - 1; ++j) { /* lower half */
									for (i = 1; i <= WindCard.this.nptc - 1; ++i) {
										exes[0] = (int) (WindCard.this.fact * WindCard.this.xplg[j][i])
												+ WindCard.this.xt;
										whys[0] = (int) (WindCard.this.fact * -WindCard.this.yplg[j][i])
												+ WindCard.this.yt;
										exes[1] = (int) (WindCard.this.fact * WindCard.this.xplg[j][i + 1])
												+ WindCard.this.xt;
										whys[1] = (int) (WindCard.this.fact * -WindCard.this.yplg[j][i + 1])
												+ WindCard.this.yt;
										Sys.this.ouwPlotterImgBufGraphContext.drawLine(exes[0], whys[0], exes[1],
												whys[1]);
									}
								}
								// stagnation lines
								exes[1] = (int) (WindCard.this.fact * WindCard.this.xplg[WindCard.this.nln2][1])
										+ WindCard.this.xt;
								whys[1] = (int) (WindCard.this.fact * -WindCard.this.yplg[WindCard.this.nln2][1])
										+ WindCard.this.yt;
								for (i = 2; i <= WindCard.this.npt2 - 1; ++i) {
									exes[0] = exes[1];
									whys[0] = whys[1];
									exes[1] = (int) (WindCard.this.fact * WindCard.this.xplg[WindCard.this.nln2][i])
											+ WindCard.this.xt;
									whys[1] = (int) (WindCard.this.fact * -WindCard.this.yplg[WindCard.this.nln2][i])
											+ WindCard.this.yt;
									Sys.this.ouwPlotterImgBufGraphContext.drawLine(exes[0], whys[0], exes[1], whys[1]);
								}
								exes[1] = (int) (WindCard.this.fact
										* WindCard.this.xplg[WindCard.this.nln2][WindCard.this.npt2 + 1])
										+ WindCard.this.xt;
								whys[1] = (int) (WindCard.this.fact
										* -WindCard.this.yplg[WindCard.this.nln2][WindCard.this.npt2 + 1])
										+ WindCard.this.yt;
								for (i = WindCard.this.npt2 + 2; i <= WindCard.this.nptc; ++i) {
									exes[0] = exes[1];
									whys[0] = whys[1];
									exes[1] = (int) (WindCard.this.fact * WindCard.this.xplg[WindCard.this.nln2][i])
											+ WindCard.this.xt;
									whys[1] = (int) (WindCard.this.fact * -WindCard.this.yplg[WindCard.this.nln2][i])
											+ WindCard.this.yt;
									Sys.this.ouwPlotterImgBufGraphContext.drawLine(exes[0], whys[0], exes[1], whys[1]);
								}

								for (j = WindCard.this.nln2 + 1; j <= WindCard.this.nlnc; ++j) { /* upper half */
									for (i = 1; i <= WindCard.this.nptc - 1; ++i) {
										exes[0] = (int) (WindCard.this.fact * WindCard.this.xplg[j][i])
												+ WindCard.this.xt;
										whys[0] = (int) (WindCard.this.fact * -WindCard.this.yplg[j][i])
												+ WindCard.this.yt;
										exes[1] = (int) (WindCard.this.fact * WindCard.this.xplg[j][i + 1])
												+ WindCard.this.xt;
										whys[1] = (int) (WindCard.this.fact * -WindCard.this.yplg[j][i + 1])
												+ WindCard.this.yt;
										Sys.this.ouwPlotterImgBufGraphContext.drawLine(exes[0], whys[0], exes[1],
												whys[1]);
									}
								}
								// draw the cylinder
								Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.white);
								exes[1] = (int) (WindCard.this.fact * WindCard.this.xplg[0][WindCard.this.npt2])
										+ WindCard.this.xt;
								whys[1] = (int) (WindCard.this.fact * -WindCard.this.yplg[0][WindCard.this.npt2])
										+ WindCard.this.yt;
								exes[2] = (int) (WindCard.this.fact * WindCard.this.xplg[0][WindCard.this.npt2])
										+ WindCard.this.xt;
								whys[2] = (int) (WindCard.this.fact * -WindCard.this.yplg[0][WindCard.this.npt2])
										+ WindCard.this.yt;
								for (i = 1; i <= WindCard.this.npt2 - 1; ++i) {
									exes[0] = exes[1];
									whys[0] = whys[1];
									exes[1] = (int) (WindCard.this.fact * WindCard.this.xplg[0][WindCard.this.npt2 - i])
											+ WindCard.this.xt;
									whys[1] = (int) (WindCard.this.fact
											* -WindCard.this.yplg[0][WindCard.this.npt2 - i]) + WindCard.this.yt;
									exes[3] = exes[2];
									whys[3] = whys[2];
									exes[2] = (int) (WindCard.this.fact * WindCard.this.xplg[0][WindCard.this.npt2 + i])
											+ WindCard.this.xt;
									whys[2] = (int) (WindCard.this.fact
											* -WindCard.this.yplg[0][WindCard.this.npt2 + i]) + WindCard.this.yt;
									Sys.this.ouwPlotterImgBufGraphContext.fillPolygon(exes, whys, 4);
								}
								// draw the axes
								Sys.this.ouwPlotterImgBufGraphContext.setColor(Color.cyan);
								exes[1] = (int) (WindCard.this.fact * 0.0) + WindCard.this.xt;
								whys[1] = (int) (WindCard.this.fact * -10.0) + WindCard.this.yt;
								exes[2] = (int) (WindCard.this.fact * 0.0) + WindCard.this.xt;
								whys[2] = (int) (WindCard.this.fact * 10.0) + WindCard.this.yt;
								Sys.this.ouwPlotterImgBufGraphContext.drawLine(exes[1], whys[1], exes[2], whys[2]);
								exes[1] = (int) (WindCard.this.fact * -10.0) + WindCard.this.xt;
								whys[1] = (int) (WindCard.this.fact * 0.0) + WindCard.this.yt;
								exes[2] = (int) (WindCard.this.fact * 10.0) + WindCard.this.xt;
								whys[2] = (int) (WindCard.this.fact * 0.0) + WindCard.this.yt;
								Sys.this.ouwPlotterImgBufGraphContext.drawLine(exes[1], whys[1], exes[2], whys[2]);
								// draw the poles
								exes[1] = (int) (WindCard.this.fact * 1.0) + WindCard.this.xt;
								whys[1] = (int) (WindCard.this.fact * 0.0) + WindCard.this.yt;
								Sys.this.ouwPlotterImgBufGraphContext.drawString("*", exes[1], whys[1] + 5);
								exes[1] = (int) (WindCard.this.fact * -1.0) + WindCard.this.xt;
								whys[1] = (int) (WindCard.this.fact * 0.0) + WindCard.this.yt;
								Sys.this.ouwPlotterImgBufGraphContext.drawString("*", exes[1], whys[1] + 5);
							}
							g.drawImage(Sys.this.ouwPlotterImageBuffer, 0, 0, this);
						}
					}

					/**
					 * (non-Javadoc).
					 *
					 * @see java.lang.Runnable#run()
					 */
					@Override
					public void run() {
						int timer;

						timer = 100;
						while (true) {
							try {
								Thread.sleep(timer);
							} catch (final InterruptedException e) {
							}
							WindCard.this.ouw.plotter.repaint();
						}
					}

					/** Start. */
					public void start() {
						if (this.run2 == null) {
							this.run2 = new Thread(this);
							this.run2.start();
						}
						Sys.logger.info("Wind Ouw Plotter Started");
					}

					/**
					 * (non-Javadoc).
					 *
					 * @param g
					 *            the g
					 * @see java.awt.Canvas#update(java.awt.Graphics)
					 */
					@Override
					public void update(Graphics g) {
						WindCard.this.ouw.plotter.paint(g);
					}
				} // Plt

				/** The Class Perf. */
				class Perf extends Panel {

					/** The Constant serialVersionUID. */
					private static final long serialVersionUID = 1L;

					/** The outerparent. */
					// private Sys outerparent;

					/** The prnt. */
					private TextArea prnt;

					/**
					 * Instantiates a new perf.
					 *
					 * @param target
					 *            the target
					 */
					Perf(Sys target) {

						this.setLayout(new GridLayout(1, 1, 0, 0));

						this.prnt = new TextArea();
						this.prnt.setEditable(false);

						this.prnt.append("Sysnel - Wind Tunnel 1.0 beta - 10 Sept 08 ");
						this.add(this.prnt);
					}
				} // Perf

				/** The Constant serialVersionUID. */
				private static final long serialVersionUID = 1L;

				/** The grf. */
				Grf grf;

				/** The outerparent. */
				Sys outerparent;

				/** The perf. */
				Perf perf;

				/** The plt. */
				OuwPlotter plotter;

				/**
				 * Instantiates a new ouw.
				 *
				 * @param target
				 *            the target
				 */
				Ouw(Sys target) {
					this.outerparent = target;
					WindCard.this.layout = new CardLayout();
					this.setLayout(WindCard.this.layout);

					this.plotter = new OuwPlotter(this.outerparent);
					this.grf = new Grf(this.outerparent);
					this.perf = new Perf(this.outerparent);

					this.add(MainPanel.FIRST_CARD, this.plotter);
					this.add(MainPanel.THIRD_CARD, this.perf);
					this.add(MainPanel.FOURTH_CARD, this.grf);
				}
			} // Ouw

			/** The Class Solvew. */
			class SolveWind {

				/** Instantiates a new solvew. */
				SolveWind() {
				}

				/** Gen flow. */
				public void genFlow() { // generate flowfield
					double rnew, thet, psv, fxg;
					int k, index;
					/* all lines of flow except stagnation line */
					for (k = 1; k <= WindCard.this.nlnc; ++k) {
						psv = -.5 * (WindCard.this.nln2 - 1) + .5 * (k - 1);
						fxg = WindCard.this.xflow;
						for (index = 1; index <= WindCard.this.nptc; ++index) {
							WindCard.this.solvew.getPoints(fxg, psv);
							WindCard.this.xg[k][index] = WindCard.this.lxgt;
							WindCard.this.yg[k][index] = WindCard.this.lygt;
							WindCard.this.rg[k][index] = WindCard.this.lrgt;
							WindCard.this.thg[k][index] = WindCard.this.lthgt;
							WindCard.this.xm[k][index] = WindCard.this.lxmt;
							WindCard.this.ym[k][index] = WindCard.this.lymt;
							if (WindCard.this.anflag == 1) { // stall model
								if (WindCard.this.alfval > 10.0 && psv > 0.0) {
									if (WindCard.this.xm[k][index] > 0.0) {
										WindCard.this.ym[k][index] = WindCard.this.ym[k][index - 1];
									}
								}
								if (WindCard.this.alfval < -10.0 && psv < 0.0) {
									if (WindCard.this.xm[k][index] > 0.0) {
										WindCard.this.ym[k][index] = WindCard.this.ym[k][index - 1];
									}
								}
							}
							WindCard.this.solvew.getVel(WindCard.this.lrg, WindCard.this.lthg);
							fxg = fxg + WindCard.this.vxdir * WindCard.this.deltb;
							WindCard.this.xgc[k][index] = WindCard.this.lxgtc;
							WindCard.this.ygc[k][index] = WindCard.this.lygtc;
						}
					}
					/* stagnation line */
					k = WindCard.this.nln2;
					psv = 0.0;
					/* incoming flow */
					for (index = 1; index <= WindCard.this.npt2; ++index) {
						rnew = 20.0 - (20.0 - WindCard.this.rval)
								* Math.sin(WindCard.this.pid2 * (index - 1) / (WindCard.this.npt2 - 1));
						thet = Math.asin(.999 * (psv - WindCard.this.gamval * Math.log(rnew / WindCard.this.rval))
								/ (rnew - WindCard.this.rval * WindCard.this.rval / rnew));
						fxg = -rnew * Math.cos(thet);
						WindCard.this.solvew.getPoints(fxg, psv);
						WindCard.this.xg[k][index] = WindCard.this.lxgt;
						WindCard.this.yg[k][index] = WindCard.this.lygt;
						WindCard.this.rg[k][index] = WindCard.this.lrgt;
						WindCard.this.thg[k][index] = WindCard.this.lthgt;
						WindCard.this.xm[k][index] = WindCard.this.lxmt;
						WindCard.this.ym[k][index] = WindCard.this.lymt;
						WindCard.this.xgc[k][index] = WindCard.this.lxgtc;
						WindCard.this.ygc[k][index] = WindCard.this.lygtc;
					}
					/* downstream flow */
					for (index = 1; index <= WindCard.this.npt2; ++index) {
						rnew = 20.0 + .01 - (20.0 - WindCard.this.rval)
								* Math.cos(WindCard.this.pid2 * (index - 1) / (WindCard.this.npt2 - 1));
						thet = Math.asin(.999 * (psv - WindCard.this.gamval * Math.log(rnew / WindCard.this.rval))
								/ (rnew - WindCard.this.rval * WindCard.this.rval / rnew));
						fxg = rnew * Math.cos(thet);
						WindCard.this.solvew.getPoints(fxg, psv);
						WindCard.this.xg[k][WindCard.this.npt2 + index] = WindCard.this.lxgt;
						WindCard.this.yg[k][WindCard.this.npt2 + index] = WindCard.this.lygt;
						WindCard.this.rg[k][WindCard.this.npt2 + index] = WindCard.this.lrgt;
						WindCard.this.thg[k][WindCard.this.npt2 + index] = WindCard.this.lthgt;
						WindCard.this.xm[k][WindCard.this.npt2 + index] = WindCard.this.lxmt;
						WindCard.this.ym[k][WindCard.this.npt2 + index] = WindCard.this.lymt;
						WindCard.this.xgc[k][index] = WindCard.this.lxgtc;
						WindCard.this.ygc[k][index] = WindCard.this.lygtc;
					}
					/* stagnation point */
					WindCard.this.xg[k][WindCard.this.npt2] = WindCard.this.xcval;
					WindCard.this.yg[k][WindCard.this.npt2] = WindCard.this.ycval;
					WindCard.this.rg[k][WindCard.this.npt2] = Math.sqrt(
							WindCard.this.xcval * WindCard.this.xcval + WindCard.this.ycval * WindCard.this.ycval);
					WindCard.this.thg[k][WindCard.this.npt2] = Math.atan2(WindCard.this.ycval, WindCard.this.xcval)
							/ WindCard.this.convdr;
					WindCard.this.xm[k][WindCard.this.npt2] = (WindCard.this.xm[k][WindCard.this.npt2 + 1]
							+ WindCard.this.xm[k][WindCard.this.npt2 - 1]) / 2.0;
					WindCard.this.ym[k][WindCard.this.npt2] = (WindCard.this.ym[0][WindCard.this.nptc / 4 + 1]
							+ WindCard.this.ym[0][WindCard.this.nptc / 4 * 3 + 1]) / 2.0;
					/* compute lift coefficient */
					WindCard.this.leg = WindCard.this.xcval - Math
							.sqrt(WindCard.this.rval * WindCard.this.rval - WindCard.this.ycval * WindCard.this.ycval);
					WindCard.this.teg = WindCard.this.xcval + Math
							.sqrt(WindCard.this.rval * WindCard.this.rval - WindCard.this.ycval * WindCard.this.ycval);
					WindCard.this.lem = WindCard.this.leg + 1.0 / WindCard.this.leg;
					WindCard.this.tem = WindCard.this.teg + 1.0 / WindCard.this.teg;
					WindCard.this.chrd = WindCard.this.tem - WindCard.this.lem;
					WindCard.this.clift = WindCard.this.gamval * 4.0 * Sys.PI / WindCard.this.chrd;

					return;
				}

				/**
				 * Gets the circ.
				 *
				 * @return the circ
				 */
				public void getCirculation() { // circulation from Kutta condition

					WindCard.this.xcval = 0.0;
					switch (WindCard.this.foiltype) {
					case FOILTYPE_JUOKOWSKI: { /* Juokowski geometry */
						juokowskiGeometry();
						break;
					}
					case FOILTYPE_ELLIPTICAL: { /* Elliptical geometry */
						ellipticalGeometry();
						break;
					}
					case FOILTYPE_PLATE: { /* Plate geometry */
						plateGeometry();
						break;
					}
					case FOILTYPE_CYLINDER: { /* get circulation for rotating cylnder */
						cylinderGeometry();
						break;
					}
					case FOILTYPE_BALL: { /* get circulation for rotating ball */
						cylinderGeometry();
						break;
					}
					}

					return;
				}

				/**
				 * Cylinder geometry.
				 */
				private void cylinderGeometry() {
					WindCard.this.rval = WindCard.this.radius / WindCard.this.lconv;
					WindCard.this.gamval = 4.0 * Sys.PI * Sys.PI * WindCard.this.spin * WindCard.this.rval
							* WindCard.this.rval / (WindCard.this.vfsd / WindCard.this.vconv);
					WindCard.this.gamval = WindCard.this.gamval * WindCard.this.spindr;
					WindCard.this.ycval = .0001;
				}

				/**
				 * Plate geometry.
				 */
				private void plateGeometry() {
					double beta;
					WindCard.this.ycval = WindCard.this.camval / 2.0;
					WindCard.this.rval = Math.sqrt(WindCard.this.ycval * WindCard.this.ycval + 1.0);
					beta = Math.asin(WindCard.this.ycval / WindCard.this.rval)
							/ WindCard.this.convdr; /* Kutta condition */
					WindCard.this.gamval = 2.0 * WindCard.this.rval
							* Math.sin((WindCard.this.alfval + beta) * WindCard.this.convdr);
				}

				/**
				 * Elliptical geometry.
				 */
				private void ellipticalGeometry() {
					double beta;
					WindCard.this.ycval = WindCard.this.camval / 2.0;
					WindCard.this.rval = WindCard.this.thkval / 4.0
							+ Math.sqrt(WindCard.this.thkval * WindCard.this.thkval / 16.0
									+ WindCard.this.ycval * WindCard.this.ycval + 1.0);
					beta = Math.asin(WindCard.this.ycval / WindCard.this.rval)
							/ WindCard.this.convdr; /* Kutta condition */
					WindCard.this.gamval = 2.0 * WindCard.this.rval
							* Math.sin((WindCard.this.alfval + beta) * WindCard.this.convdr);
				}

				/**
				 * Juokowski geometry.
				 */
				private void juokowskiGeometry() {
					double beta;
					WindCard.this.ycval = WindCard.this.camval / 2.0;
					WindCard.this.rval = WindCard.this.thkval / 4.0
							+ Math.sqrt(WindCard.this.thkval * WindCard.this.thkval / 16.0
									+ WindCard.this.ycval * WindCard.this.ycval + 1.0);
					WindCard.this.xcval = 1.0 - Math
							.sqrt(WindCard.this.rval * WindCard.this.rval - WindCard.this.ycval * WindCard.this.ycval);
					beta = Math.asin(WindCard.this.ycval / WindCard.this.rval)
							/ WindCard.this.convdr; /* Kutta condition */
					WindCard.this.gamval = 2.0 * WindCard.this.rval
							* Math.sin((WindCard.this.alfval + beta) * WindCard.this.convdr);
				}

				/**
				 * Gets the free stream.
				 *
				 * @return the free stream
				 */
				public void getFreeStream() { // free stream conditions
					double hite; /* MODS 19 Jan 00 whole routine */
					double rgas;

					WindCard.this.g0 = 32.2;
					rgas = 1718.; /* ft2/sec2 R */
					hite = WindCard.this.alt / WindCard.this.lconv;
					WindCard.this.ps0 = WindCard.this.psin / WindCard.this.piconv;
					WindCard.this.ts0 = WindCard.this.tsin / WindCard.this.ticonv;
					if (WindCard.this.planet == 0) { // Earth standard day
						if (hite <= 36152.) { // Troposphere
							WindCard.this.ts0 = 518.6 - 3.56 * hite / 1000.;
							WindCard.this.ps0 = 2116.217 * Math.pow(WindCard.this.ts0 / 518.6, 5.256);
						}
						if (hite >= 36152. && hite <= 82345.) { // Stratosphere
							WindCard.this.ts0 = 389.98;
							WindCard.this.ps0 = 2116.217 * .2236 * Math.exp((36000. - hite) / (53.35 * 389.98));
						}
						if (hite >= 82345.) {
							WindCard.this.ts0 = 389.98 + 1.645 * (hite - 82345) / 1000.;
							WindCard.this.ps0 = 2116.217 * .02456 * Math.pow(WindCard.this.ts0 / 389.98, -11.388);
						}
						WindCard.this.temf = WindCard.this.ts0 - 459.6;
						if (WindCard.this.temf <= 0.0) {
							WindCard.this.temf = 0.0;
						}
						/*
						 * Eq 1:6A Domasch - effect of humidity rlhum = 0.0 ; presm = ps0 * 29.92 /
						 * 2116.217 ; pvap = rlhum*(2.685+.00353*Math.pow(temf,2.245)); rho = (ps0 -
						 * .379*pvap)/(rgas * ts0) ;
						 */
						WindCard.this.rho = WindCard.this.ps0 / (rgas * WindCard.this.ts0);
					}

					if (WindCard.this.planet == 1) { // Mars - curve fit of orbiter data
						rgas = 1149.; /* ft2/sec2 R */
						if (hite <= 22960.) {
							WindCard.this.ts0 = 434.02 - .548 * hite / 1000.;
							WindCard.this.ps0 = 14.62 * Math.pow(2.71828, -.00003 * hite);
						}
						if (hite > 22960.) {
							WindCard.this.ts0 = 449.36 - 1.217 * hite / 1000.;
							WindCard.this.ps0 = 14.62 * Math.pow(2.71828, -.00003 * hite);
						}
						WindCard.this.rho = WindCard.this.ps0 / (rgas * WindCard.this.ts0);
					}

					if (WindCard.this.planet == 2) { // water -- constant density
						hite = -WindCard.this.alt / WindCard.this.lconv;
						WindCard.this.ts0 = 520.;
						WindCard.this.rho = 1.94;
						WindCard.this.ps0 = 2116.217 - WindCard.this.rho * WindCard.this.g0 * hite;
					}

					if (WindCard.this.planet == 3) { // specify air temp and pressure
						WindCard.this.rho = WindCard.this.ps0 / (rgas * WindCard.this.ts0);
					}

					if (WindCard.this.planet == 4) { // specify fluid density
						WindCard.this.ps0 = 2116.217;
					}

					WindCard.this.q0 = .5 * WindCard.this.rho * WindCard.this.vfsd * WindCard.this.vfsd
							/ (WindCard.this.vconv * WindCard.this.vconv);
					WindCard.this.pt0 = WindCard.this.ps0 + WindCard.this.q0;

					return;
				}

				/**
				 * Gets the geom.
				 *
				 * @return the geom
				 */
				public void getGometry() { // geometry
					double thet, rdm, thtm;
					int index;

					for (index = 1; index <= WindCard.this.nptc; ++index) {
						thet = (index - 1) * 360. / (WindCard.this.nptc - 1);
						WindCard.this.xg[0][index] = WindCard.this.rval * Math.cos(WindCard.this.convdr * thet)
								+ WindCard.this.xcval;
						WindCard.this.yg[0][index] = WindCard.this.rval * Math.sin(WindCard.this.convdr * thet)
								+ WindCard.this.ycval;
						WindCard.this.rg[0][index] = Math.sqrt(WindCard.this.xg[0][index] * WindCard.this.xg[0][index]
								+ WindCard.this.yg[0][index] * WindCard.this.yg[0][index]);
						WindCard.this.thg[0][index] = Math.atan2(WindCard.this.yg[0][index], WindCard.this.xg[0][index])
								/ WindCard.this.convdr;
						WindCard.this.xm[0][index] = (WindCard.this.rg[0][index] + 1.0 / WindCard.this.rg[0][index])
								* Math.cos(WindCard.this.convdr * WindCard.this.thg[0][index]);
						WindCard.this.ym[0][index] = (WindCard.this.rg[0][index] - 1.0 / WindCard.this.rg[0][index])
								* Math.sin(WindCard.this.convdr * WindCard.this.thg[0][index]);
						rdm = Math.sqrt(WindCard.this.xm[0][index] * WindCard.this.xm[0][index]
								+ WindCard.this.ym[0][index] * WindCard.this.ym[0][index]);
						thtm = Math.atan2(WindCard.this.ym[0][index], WindCard.this.xm[0][index])
								/ WindCard.this.convdr;
						WindCard.this.xm[0][index] = rdm
								* Math.cos((thtm - WindCard.this.alfval) * WindCard.this.convdr);
						WindCard.this.ym[0][index] = rdm
								* Math.sin((thtm - WindCard.this.alfval) * WindCard.this.convdr);
						this.getVel(WindCard.this.rval, thet);
						WindCard.this.plp[index] = (WindCard.this.ps0 + WindCard.this.pres * WindCard.this.q0)
								/ 2116.217 * WindCard.this.pconv;
						WindCard.this.plv[index] = WindCard.this.vel * WindCard.this.vfsd;
						WindCard.this.xgc[0][index] = WindCard.this.rval * Math.cos(WindCard.this.convdr * thet)
								+ WindCard.this.xcval;
						WindCard.this.ygc[0][index] = WindCard.this.rval * Math.sin(WindCard.this.convdr * thet)
								+ WindCard.this.ycval;
					}

					WindCard.this.xt1 = WindCard.this.xt + WindCard.this.spanfac;
					WindCard.this.yt1 = WindCard.this.yt - WindCard.this.spanfac;
					WindCard.this.xt2 = WindCard.this.xt - WindCard.this.spanfac;
					WindCard.this.yt2 = WindCard.this.yt + WindCard.this.spanfac;

					return;
				}

				/**
				 * Gets the points.
				 *
				 * @param fxg
				 *            the fxg
				 * @param psv
				 *            the psv
				 * @return the points
				 */
				public void getPoints(double fxg, double psv) { // flow in x-psi
					double radm, thetm; /* MODS 20 Jul 99 whole routine */
					double fnew, ynew, yold, rfac, deriv;
					int iter;
					/* get variables in the generating plane */
					/* iterate to find value of yg */
					ynew = 10.0;
					yold = 10.0;
					if (psv < 0.0) {
						ynew = -10.0;
					}
					if (Math.abs(psv) < .001 && WindCard.this.alfval < 0.0) {
						ynew = WindCard.this.rval;
					}
					if (Math.abs(psv) < .001 && WindCard.this.alfval >= 0.0) {
						ynew = -WindCard.this.rval;
					}
					fnew = 0.1;
					iter = 1;
					while (Math.abs(fnew) >= .00001 && iter < 25) {
						++iter;
						rfac = fxg * fxg + ynew * ynew;
						if (rfac < WindCard.this.rval * WindCard.this.rval) {
							rfac = WindCard.this.rval * WindCard.this.rval + .01;
						}
						fnew = psv - ynew * (1.0 - WindCard.this.rval * WindCard.this.rval / rfac)
								- WindCard.this.gamval * Math.log(Math.sqrt(rfac) / WindCard.this.rval);
						deriv = -(1.0 - WindCard.this.rval * WindCard.this.rval / rfac)
								- 2.0 * ynew * ynew * WindCard.this.rval * WindCard.this.rval / (rfac * rfac)
								- WindCard.this.gamval * ynew / rfac;
						yold = ynew;
						ynew = yold - .5 * fnew / deriv;
					}
					WindCard.this.lyg = yold;
					/* rotate for angle of attack */
					WindCard.this.lrg = Math.sqrt(fxg * fxg + WindCard.this.lyg * WindCard.this.lyg);
					WindCard.this.lthg = Math.atan2(WindCard.this.lyg, fxg) / WindCard.this.convdr;
					WindCard.this.lxgt = WindCard.this.lrg
							* Math.cos(WindCard.this.convdr * (WindCard.this.lthg + WindCard.this.alfval));
					WindCard.this.lygt = WindCard.this.lrg
							* Math.sin(WindCard.this.convdr * (WindCard.this.lthg + WindCard.this.alfval));
					/* translate cylinder to generate airfoil */
					WindCard.this.lxgtc = WindCard.this.lxgt = WindCard.this.lxgt + WindCard.this.xcval;
					WindCard.this.lygtc = WindCard.this.lygt = WindCard.this.lygt + WindCard.this.ycval;
					WindCard.this.lrgt = Math
							.sqrt(WindCard.this.lxgt * WindCard.this.lxgt + WindCard.this.lygt * WindCard.this.lygt);
					WindCard.this.lthgt = Math.atan2(WindCard.this.lygt, WindCard.this.lxgt) / WindCard.this.convdr;
					/* Kutta-Joukowski mapping */
					WindCard.this.lxm = (WindCard.this.lrgt + 1.0 / WindCard.this.lrgt)
							* Math.cos(WindCard.this.convdr * WindCard.this.lthgt);
					WindCard.this.lym = (WindCard.this.lrgt - 1.0 / WindCard.this.lrgt)
							* Math.sin(WindCard.this.convdr * WindCard.this.lthgt);
					/* tranforms for view fixed with free stream */
					/* take out rotation for angle of attack mapped and cylinder */
					radm = Math.sqrt(WindCard.this.lxm * WindCard.this.lxm + WindCard.this.lym * WindCard.this.lym);
					thetm = Math.atan2(WindCard.this.lym, WindCard.this.lxm) / WindCard.this.convdr;
					WindCard.this.lxmt = radm * Math.cos(WindCard.this.convdr * (thetm - WindCard.this.alfval));
					WindCard.this.lymt = radm * Math.sin(WindCard.this.convdr * (thetm - WindCard.this.alfval));

					WindCard.this.lxgt = WindCard.this.lxgt - WindCard.this.xcval;
					WindCard.this.lygt = WindCard.this.lygt - WindCard.this.ycval;
					WindCard.this.lrgt = Math
							.sqrt(WindCard.this.lxgt * WindCard.this.lxgt + WindCard.this.lygt * WindCard.this.lygt);
					WindCard.this.lthgt = Math.atan2(WindCard.this.lygt, WindCard.this.lxgt) / WindCard.this.convdr;
					WindCard.this.lxgt = WindCard.this.lrgt
							* Math.cos((WindCard.this.lthgt - WindCard.this.alfval) * WindCard.this.convdr);
					WindCard.this.lygt = WindCard.this.lrgt
							* Math.sin((WindCard.this.lthgt - WindCard.this.alfval) * WindCard.this.convdr);

					return;
				}

				/**
				 * Gets the probe.
				 *
				 * @return the probe
				 */
				public void getProbe() { /* all of the information needed for the probe */
					double prxg;
					int index;
					/* get variables in the generating plane */
					if (Math.abs(WindCard.this.ypval) < .01) {
						WindCard.this.ypval = .05;
					}
					WindCard.this.solvew.getPoints(WindCard.this.xpval, WindCard.this.ypval);

					WindCard.this.solvew.getVel(WindCard.this.lrg, WindCard.this.lthg);
					WindCard.this.loadProbe();

					WindCard.this.pxg = WindCard.this.lxgt;
					WindCard.this.pyg = WindCard.this.lygt;
					WindCard.this.prg = WindCard.this.lrgt;
					WindCard.this.pthg = WindCard.this.lthgt;
					WindCard.this.pxm = WindCard.this.lxmt;
					WindCard.this.pym = WindCard.this.lymt;
					/* smoke */
					if (WindCard.this.pboflag == PROBLE_SMOKE) {
						prxg = WindCard.this.xpval;
						for (index = 1; index <= WindCard.this.nptc; ++index) {
							WindCard.this.solvew.getPoints(prxg, WindCard.this.ypval);
							WindCard.this.xg[19][index] = WindCard.this.lxgt;
							WindCard.this.yg[19][index] = WindCard.this.lygt;
							WindCard.this.rg[19][index] = WindCard.this.lrgt;
							WindCard.this.thg[19][index] = WindCard.this.lthgt;
							WindCard.this.xm[19][index] = WindCard.this.lxmt;
							WindCard.this.ym[19][index] = WindCard.this.lymt;
							if (WindCard.this.anflag == 1) { // stall model
								if (WindCard.this.xpval > 0.0) {
									if (WindCard.this.alfval > 10.0 && WindCard.this.ypval > 0.0) {
										WindCard.this.ym[19][index] = WindCard.this.ym[19][1];
									}
									if (WindCard.this.alfval < -10.0 && WindCard.this.ypval < 0.0) {
										WindCard.this.ym[19][index] = WindCard.this.ym[19][1];
									}
								}
								if (WindCard.this.xpval < 0.0) {
									if (WindCard.this.alfval > 10.0 && WindCard.this.ypval > 0.0) {
										if (WindCard.this.xm[19][index] > 0.0) {
											WindCard.this.ym[19][index] = WindCard.this.ym[19][index - 1];
										}
									}
									if (WindCard.this.alfval < -10.0 && WindCard.this.ypval < 0.0) {
										if (WindCard.this.xm[19][index] > 0.0) {
											WindCard.this.ym[19][index] = WindCard.this.ym[19][index - 1];
										}
									}
								}
							}
							WindCard.this.solvew.getVel(WindCard.this.lrg, WindCard.this.lthg);
							prxg = prxg + WindCard.this.vxdir * WindCard.this.deltb;
						}
					}
					return;
				}

				/**
				 * Gets the vel.
				 *
				 * @param rad
				 *            the rad
				 * @param theta
				 *            the theta
				 * @return the vel
				 */
				public void getVel(double rad, double theta) { // velocity and pressure
					double ur, uth, jake1, jake2, jakesq;
					double xloc, yloc, thrad, alfrad;

					thrad = WindCard.this.convdr * theta;
					alfrad = WindCard.this.convdr * WindCard.this.alfval;
					/* get x, y location in cylinder plane */
					xloc = rad * Math.cos(thrad);
					yloc = rad * Math.sin(thrad);
					/* velocity in cylinder plane */
					ur = Math.cos(thrad - alfrad) * (1.0 - WindCard.this.rval * WindCard.this.rval / (rad * rad));
					uth = -Math.sin(thrad - alfrad) * (1.0 + WindCard.this.rval * WindCard.this.rval / (rad * rad))
							- WindCard.this.gamval / rad;
					WindCard.this.usq = ur * ur + uth * uth;
					WindCard.this.vxdir = ur * Math.cos(thrad) - uth * Math.sin(thrad); // MODS 20 Jul 99
					/* translate to generate airfoil */
					xloc = xloc + WindCard.this.xcval;
					yloc = yloc + WindCard.this.ycval;
					/* compute new radius-theta */
					rad = Math.sqrt(xloc * xloc + yloc * yloc);
					thrad = Math.atan2(yloc, xloc);
					/* compute Joukowski Jacobian */
					jake1 = 1.0 - Math.cos(2.0 * thrad) / (rad * rad);
					jake2 = Math.sin(2.0 * thrad) / (rad * rad);
					jakesq = jake1 * jake1 + jake2 * jake2;
					if (Math.abs(jakesq) <= .01) {
						jakesq = .01; /* protection */
					}
					WindCard.this.vsq = WindCard.this.usq / jakesq;
					/* vel is velocity ratio - pres is coefficient (p-p0)/q0 */
					if (WindCard.this.foiltype <= FOILTYPE_PLATE) {
						WindCard.this.vel = Math.sqrt(WindCard.this.vsq);
						WindCard.this.pres = 1.0 - WindCard.this.vsq;
					}
					if (WindCard.this.foiltype >= FOILTYPE_CYLINDER) {
						WindCard.this.vel = Math.sqrt(WindCard.this.usq);
						WindCard.this.pres = 1.0 - WindCard.this.usq;
					}
					return;
				}

				/** Sets the defaults. */
				public void setDefaults() {

					Sys.this.datnum = 0;
					WindCard.this.dato = 0;
					WindCard.this.datp = 0;
					WindCard.this.arcor = 0;
					WindCard.this.lunits = 0;
					WindCard.this.lftout = 0;
					WindCard.this.inptopt = 0;
					WindCard.this.outopt = 0;
					WindCard.this.nummod = 1;
					WindCard.this.nlnc = 45;
					WindCard.this.nln2 = WindCard.this.nlnc / 2 + 1;
					WindCard.this.nptc = 75;
					WindCard.this.npt2 = WindCard.this.nptc / 2 + 1;
					WindCard.this.deltb = .5;
					WindCard.this.foiltype = FOILTYPE_JUOKOWSKI;
					WindCard.this.flflag = 1;
					WindCard.this.thkval = .5;
					WindCard.this.thkinpt = 12.5; /* MODS 10 SEP 99 */
					WindCard.this.camval = 0.0;
					WindCard.this.caminpt = 0.0;
					WindCard.this.alfval = 0.0;
					WindCard.this.gamval = 0.0;
					WindCard.this.radius = 1.0;
					WindCard.this.spin = 0.0;
					WindCard.this.spindr = 1.0;
					WindCard.this.rval = 1.0;
					WindCard.this.ycval = 0.0;
					WindCard.this.xcval = 0.0;
					WindCard.this.displ = 1;
					WindCard.this.viewflg = WALL_VIEW_SOLID;
					WindCard.this.dispp = 0;
					WindCard.this.calcrange = 0;
					WindCard.this.dout = 0;
					WindCard.this.stfact = 1.0;
					// factp = factp * chord/chrdold ;

					WindCard.this.xpval = 2.1;
					WindCard.this.ypval = -.5;
					WindCard.this.pboflag = PROBE_OFF;
					WindCard.this.xflow = -20.0; /* MODS 20 Jul 99 */

					WindCard.this.pconv = 14.696;
					WindCard.this.piconv = 1.0;
					WindCard.this.ticonv = 1.0;
					WindCard.this.pmin = .5;
					WindCard.this.pmax = 1.0;
					WindCard.this.fconv = 1.0;
					WindCard.this.fmax = 100000.;
					WindCard.this.fmaxb = .50;
					WindCard.this.vconv = .6818;
					WindCard.this.vfsd = 0.0;
					WindCard.this.vmax = 250.;
					WindCard.this.lconv = 1.0;

					WindCard.this.planet = 3;
					WindCard.this.psin = 2116.217;
					WindCard.this.psmin = 100;
					WindCard.this.psmax = 2500.;
					WindCard.this.tsin = 518.6;
					WindCard.this.tsmin = 350.;
					WindCard.this.tsmax = 660.;
					WindCard.this.alt = 0.0;
					WindCard.this.altmax = 50000.;
					WindCard.this.chrdold = WindCard.this.chord = 1.0;
					WindCard.this.spnold = WindCard.this.span = 3.281;
					WindCard.this.aspr = WindCard.this.span / WindCard.this.chord;
					WindCard.this.arold = WindCard.this.area = .5 * 3.281;
					WindCard.this.armax = 2500.01;
					WindCard.this.armin = .01; /* MODS 9 SEP 99 */

					WindCard.this.xt = 205;
					WindCard.this.yt = 140;
					// xt = 225; yt = 140;
					WindCard.this.sldloc = 52;
					WindCard.this.xtp = 95;
					WindCard.this.ytp = 240;
					WindCard.this.factp = 35.0;
					// xtp = 95; ytp = 240; factp = 40.0 ;
					WindCard.this.chrdfac = Math.sqrt(WindCard.this.chord); // Adds a chord factor to scale the foil
																			// according
					// to the chord
					WindCard.this.fact = 32.0 * WindCard.this.chrdfac; // chord is the only factor for the fact variable
					WindCard.this.spanfac = (int) (4.0 * 50.0 * .3535);
					WindCard.this.xt1 = WindCard.this.xt + WindCard.this.spanfac;
					WindCard.this.yt1 = WindCard.this.yt - WindCard.this.spanfac;
					WindCard.this.xt2 = WindCard.this.xt - WindCard.this.spanfac;
					WindCard.this.yt2 = WindCard.this.yt + WindCard.this.spanfac;
					WindCard.this.plthg[1] = 0.0;

					WindCard.this.probflag = 2;
					WindCard.this.anflag = 1;
					WindCard.this.vmn = 0.0;
					WindCard.this.vmx = 250.0;
					WindCard.this.almn = 0.0;
					WindCard.this.almx = 50000.0;
					WindCard.this.angmn = -20.0;
					WindCard.this.angmx = 20.0;
					WindCard.this.camn = -25.0;
					WindCard.this.camx = 25.0;
					WindCard.this.thkmn = 1.0;
					WindCard.this.thkmx = 26.0;
					WindCard.this.chrdmn = 5. / (2.54 * 12);
					WindCard.this.chrdmx = 3.281;
					WindCard.this.armn = .01;
					WindCard.this.armx = 2500.01;
					WindCard.this.spinmn = -1500.0;
					WindCard.this.spinmx = 1500.0;
					WindCard.this.radmn = .05;
					WindCard.this.radmx = 5.0;
					WindCard.this.psmn = WindCard.this.psmin;
					WindCard.this.psmx = WindCard.this.psmax;
					WindCard.this.tsmn = WindCard.this.tsmin;
					WindCard.this.tsmx = WindCard.this.tsmax;

					WindCard.this.laby = Sys.PRESS;
					WindCard.this.labyu = Sys.PSI;
					WindCard.this.labx = Sys.X2;
					WindCard.this.labxu = Sys.CHORD3;
					WindCard.this.iprint = 0;

					return;
				}
			} // end Solvew

			/** The Class Viewer. */
			class WindViewer extends Canvas implements Runnable {

				/** The Constant serialVersionUID. */
				private static final long serialVersionUID = 1L;

				/** The anchor. */
				// private Point locate;
				// private Point anchor;

				/** The outerparent. */
				// private Sys outerparent;

				/** The runner. */
				private Thread runner;

				/**
				 * Instantiates a new viewer.
				 *
				 * @param target
				 *            the target
				 */
				WindViewer(Sys target) {
					this.setBackground(Color.black);
					this.runner = null;
				}

				/**
				 * Handle.
				 *
				 * @param x
				 *            the x
				 * @param y
				 *            the y
				 */
				public void handle(int x, int y) {
					// determine location
					if (y >= 30) {
						/*
						 * if (x >= 30 ) { if (displ != 2) { locate = new Point(x,y) ; yt = yt + (int)
						 * (.2*(locate.y - anchor.y)) ; xt = xt + (int) (.4*(locate.x - anchor.x)) ; if
						 * (xt > 320) xt = 320 ; if (xt < -280) xt = -280 ; if (yt > 300) yt = 300 ; if
						 * (yt <-300) yt = -300 ; xt1 = xt + spanfac ; yt1 = yt - spanfac ; xt2 = xt -
						 * spanfac; yt2 = yt + spanfac ; } if(displ == 2) { // move the rake locate =
						 * new Point(x,y) ; xflow = xflow + .01*(locate.x - anchor.x) ; if (xflow <
						 * -10.0) xflow = -10.0 ; if (xflow > 0.0) xflow = 0.0 ; computeFlow() ; } } if
						 * (x < 30 ) { sldloc = y ; if (sldloc < 30) sldloc = 30; if (sldloc > 165)
						 * sldloc = 165; fact = 10.0 + (sldloc-30)*1.0 ; spanfac =
						 * (int)(2.0*fact*1.0*.3535) ; xt1 = xt + spanfac ; yt1 = yt - spanfac ; xt2 =
						 * xt - spanfac; yt2 = yt + spanfac ; } conw.dwn.o1.setText(String.valueOf(xt))
						 * ; conw.dwn.o2.setText(String.valueOf(yt)) ;
						 * conw.dwn.o3.setText(String.valueOf(sldloc)) ;
						 * conw.dwn.o4.setText(String.valueOf(fact)) ;
						 */

					}
				}

				/**
				 * Handleb.
				 *
				 * @param x
				 *            the x
				 * @param y
				 *            the y
				 */
				public void handleb(int x, int y) {
					if (y >= 300) {
						if (x >= 82 && x <= 232) { // transparent wall
							WindCard.this.viewflg = WALL_VIEW_TRANSPARENT;
						}
						if (x >= 240 && x <= 390) { // solid wall
							WindCard.this.viewflg = WALL_VIEW_SOLID;
						}
					}
					WindCard.this.view.repaint();
				}

				/**
				 * Insets.
				 *
				 * @return the insets
				 */
				public Insets insets() {
					return new Insets(0, 5, 0, 5);
				}

				/**
				 * (non-Javadoc).
				 *
				 * @param evt
				 *            the evt
				 * @param x
				 *            the x
				 * @param y
				 *            the y
				 * @return true, if successful
				 * @see java.awt.Component#mouseDown(java.awt.Event, int, int)
				 */
				@Override
				public boolean mouseDown(Event evt, int x, int y) {
					// this.anchor = new Point(x, y);
					return true;
				}

				/**
				 * (non-Javadoc).
				 *
				 * @param evt
				 *            the evt
				 * @param x
				 *            the x
				 * @param y
				 *            the y
				 * @return true, if successful
				 * @see java.awt.Component#mouseDrag(java.awt.Event, int, int)
				 */
				@Override
				public boolean mouseDrag(Event evt, int x, int y) {
					this.handle(x, y);
					return true;
				}

				/**
				 * (non-Javadoc).
				 *
				 * @param evt
				 *            the evt
				 * @param x
				 *            the x
				 * @param y
				 *            the y
				 * @return true, if successful
				 * @see java.awt.Component#mouseUp(java.awt.Event, int, int)
				 */
				@Override
				public boolean mouseUp(Event evt, int x, int y) {
					this.handleb(x, y);
					return true;
				}

				/**
				 * (non-Javadoc).
				 *
				 * @param g
				 *            the g
				 * @see java.awt.Canvas#paint(java.awt.Graphics)
				 */
				@Override
				public void paint(Graphics g) {
					final boolean b = true;
					if (b && Sys.this.mainPannel.windCard.isVisible()) {
						int i, j;
						int n;
						int inmax;
						final int exes[] = new int[8];
						final int whys[] = new int[8];
						double slope, radvec, xvec, yvec;
						double yprs, yprs1;
						final int camx[] = new int[40];
						final int camy[] = new int[40];
						Color col;

						col = new Color(0, 0, 0);
						if (WindCard.this.planet == 0) {
							col = Color.cyan;
						}
						if (WindCard.this.planet == 1) {
							col = Color.orange;
						}
						if (WindCard.this.planet == 2) {
							col = Color.green;
						}
						if (WindCard.this.planet >= 3) {
							col = Color.cyan;
						}
						Sys.this.windViewerImgBuffGraphContext.setColor(Color.lightGray);
						Sys.this.windViewerImgBuffGraphContext.fillRect(0, 0, Sys.WIND_VIEWER_WIDTH,
								Sys.WIND_VIEWER_HEIGHT);
						Sys.this.windViewerImgBuffGraphContext.setColor(Color.black);
						exes[0] = 0;
						whys[0] = 200;
						exes[1] = 500;
						whys[1] = 250;
						exes[2] = 500;
						whys[2] = 500;
						exes[3] = 0;
						whys[3] = 500;
						Sys.this.windViewerImgBuffGraphContext.fillPolygon(exes, whys, 4);

						radvec = .5;

						if (WindCard.this.viewflg == WALL_VIEW_TRANSPARENT
								|| WindCard.this.viewflg == WALL_VIEW_SOLID) { // edge View
							if (WindCard.this.vfsd > .01) {
								/* plot airfoil flowfield */
								for (j = 1; j <= WindCard.this.nln2 - 1; ++j) { /* lower half */
									for (i = 1; i <= WindCard.this.nptc - 1; ++i) {
										exes[0] = (int) (WindCard.this.fact * WindCard.this.xpl[j][i])
												+ WindCard.this.xt;
										yprs = .1 * WindCard.this.xpl[j][i];
										yprs1 = .1 * WindCard.this.xpl[j][i + 1];
										whys[0] = (int) (WindCard.this.fact * (-WindCard.this.ypl[j][i] + yprs))
												+ WindCard.this.yt;
										slope = (WindCard.this.ypl[j][i + 1] - yprs1 - WindCard.this.ypl[j][i] + yprs)
												/ (WindCard.this.xpl[j][i + 1] - WindCard.this.xpl[j][i]);
										xvec = WindCard.this.xpl[j][i] + radvec / Math.sqrt(1.0 + slope * slope);
										yvec = WindCard.this.ypl[j][i] - yprs
												+ slope * (xvec - WindCard.this.xpl[j][i]);
										exes[1] = (int) (WindCard.this.fact * xvec) + WindCard.this.xt;
										whys[1] = (int) (WindCard.this.fact * -yvec) + WindCard.this.yt;
										if (WindCard.this.displ == 0) { /* MODS 21 JUL 99 */
											Sys.this.windViewerImgBuffGraphContext.setColor(Color.yellow);
											exes[1] = (int) (WindCard.this.fact * WindCard.this.xpl[j][i + 1])
													+ WindCard.this.xt;
											yprs = .1 * WindCard.this.xpl[j][i + 1];
											whys[1] = (int) (WindCard.this.fact * (-WindCard.this.ypl[j][i + 1] + yprs))
													+ WindCard.this.yt;
											Sys.this.windViewerImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1],
													whys[1]);
										}
										if (WindCard.this.displ == 2 && i / 3 * 3 == i) {
											Sys.this.windViewerImgBuffGraphContext.setColor(col);
											for (n = 1; n <= 4; ++n) {
												if (i == 6 + (n - 1) * 9) {
													Sys.this.windViewerImgBuffGraphContext.setColor(Color.yellow);
												}
											}
											if (i / 9 * 9 == i) {
												Sys.this.windViewerImgBuffGraphContext.setColor(Color.white);
											}
											Sys.this.windViewerImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1],
													whys[1]);
										}
										if (WindCard.this.displ == 1
												&& (i - WindCard.this.antim) / 3 * 3 == i - WindCard.this.antim) {
											if (WindCard.this.ancol == -1) { /* MODS 27 JUL 99 */
												if ((i - WindCard.this.antim) / 6 * 6 == i - WindCard.this.antim) {
													Sys.this.windViewerImgBuffGraphContext.setColor(col);
												}
												if ((i - WindCard.this.antim) / 6 * 6 != i - WindCard.this.antim) {
													Sys.this.windViewerImgBuffGraphContext.setColor(Color.white);
												}
											}
											if (WindCard.this.ancol == 1) { /* MODS 27 JUL 99 */
												if ((i - WindCard.this.antim) / 6 * 6 == i - WindCard.this.antim) {
													Sys.this.windViewerImgBuffGraphContext.setColor(Color.white);
												}
												if ((i - WindCard.this.antim) / 6 * 6 != i - WindCard.this.antim) {
													Sys.this.windViewerImgBuffGraphContext.setColor(col);
												}
											}
											Sys.this.windViewerImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1],
													whys[1]);
										}
									}
								}

								Sys.this.windViewerImgBuffGraphContext.setColor(Color.white); /* stagnation */
								exes[1] = (int) (WindCard.this.fact * WindCard.this.xpl[WindCard.this.nln2][1])
										+ WindCard.this.xt;
								yprs = .1 * WindCard.this.xpl[WindCard.this.nln2][1];
								whys[1] = (int) (WindCard.this.fact
										* (-WindCard.this.ypl[WindCard.this.nln2][1] + yprs)) + WindCard.this.yt;
								for (i = 2; i <= WindCard.this.npt2 - 1; ++i) {
									exes[0] = exes[1];
									whys[0] = whys[1];
									exes[1] = (int) (WindCard.this.fact * WindCard.this.xpl[WindCard.this.nln2][i])
											+ WindCard.this.xt;
									yprs = .1 * WindCard.this.xpl[WindCard.this.nln2][i];
									whys[1] = (int) (WindCard.this.fact
											* (-WindCard.this.ypl[WindCard.this.nln2][i] + yprs)) + WindCard.this.yt;
									if (WindCard.this.displ <= 2) { /* MODS 21 JUL 99 */
										Sys.this.windViewerImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1],
												whys[1]);
									}
								}
								exes[1] = (int) (WindCard.this.fact
										* WindCard.this.xpl[WindCard.this.nln2][WindCard.this.npt2 + 1])
										+ WindCard.this.xt;
								yprs = .1 * WindCard.this.xpl[WindCard.this.nln2][WindCard.this.npt2 + 1];
								whys[1] = (int) (WindCard.this.fact
										* (-WindCard.this.ypl[WindCard.this.nln2][WindCard.this.npt2 + 1] + yprs))
										+ WindCard.this.yt;
								for (i = WindCard.this.npt2 + 2; i <= WindCard.this.nptc; ++i) {
									exes[0] = exes[1];
									whys[0] = whys[1];
									exes[1] = (int) (WindCard.this.fact * WindCard.this.xpl[WindCard.this.nln2][i])
											+ WindCard.this.xt;
									yprs = .1 * WindCard.this.xpl[WindCard.this.nln2][i];
									whys[1] = (int) (WindCard.this.fact
											* (-WindCard.this.ypl[WindCard.this.nln2][i] + yprs)) + WindCard.this.yt;
									if (WindCard.this.displ <= 2) { /* MODS 21 JUL 99 */
										Sys.this.windViewerImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1],
												whys[1]);
									}
								}
							}
							/* probe location */
							if (WindCard.this.pboflag > PROBE_OFF && WindCard.this.pypl <= 0.0) {
								Sys.this.windViewerImgBuffGraphContext.setColor(Color.magenta);
								yprs = .1 * WindCard.this.pxpl;
								Sys.this.windViewerImgBuffGraphContext.fillOval(
										(int) (WindCard.this.fact * WindCard.this.pxpl) + WindCard.this.xt,
										(int) (WindCard.this.fact * (-WindCard.this.pypl + yprs)) + WindCard.this.yt
												- 2,
										5, 5);
								Sys.this.windViewerImgBuffGraphContext.setColor(Color.white);
								exes[0] = (int) (WindCard.this.fact * (WindCard.this.pxpl + .1)) + WindCard.this.xt;
								whys[0] = (int) (WindCard.this.fact * (-WindCard.this.pypl + yprs)) + WindCard.this.yt;
								exes[1] = (int) (WindCard.this.fact * (WindCard.this.pxpl + .5)) + WindCard.this.xt;
								whys[1] = (int) (WindCard.this.fact * (-WindCard.this.pypl + yprs)) + WindCard.this.yt;
								exes[2] = (int) (WindCard.this.fact * (WindCard.this.pxpl + .5)) + WindCard.this.xt;
								whys[2] = (int) (WindCard.this.fact * (-WindCard.this.pypl + 50.)) + WindCard.this.yt;
								Sys.this.windViewerImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1], whys[1]);
								Sys.this.windViewerImgBuffGraphContext.drawLine(exes[1], whys[1], exes[2], whys[2]);
								if (WindCard.this.pboflag == PROBLE_SMOKE && WindCard.this.vfsd >= .01) { // smoke trail
																											// ;
									Sys.this.windViewerImgBuffGraphContext.setColor(Color.green);
									for (i = 1; i <= WindCard.this.nptc - 1; ++i) {
										exes[0] = (int) (WindCard.this.fact * WindCard.this.xpl[19][i])
												+ WindCard.this.xt;
										yprs = .1 * WindCard.this.xpl[19][i];
										yprs1 = .1 * WindCard.this.xpl[19][i + 1];
										whys[0] = (int) (WindCard.this.fact * (-WindCard.this.ypl[19][i] + yprs))
												+ WindCard.this.yt;
										slope = (WindCard.this.ypl[19][i + 1] - yprs1 - WindCard.this.ypl[19][i] + yprs)
												/ (WindCard.this.xpl[19][i + 1] - WindCard.this.xpl[19][i]);
										xvec = WindCard.this.xpl[19][i] + radvec / Math.sqrt(1.0 + slope * slope);
										yvec = WindCard.this.ypl[19][i] - yprs
												+ slope * (xvec - WindCard.this.xpl[19][i]);
										exes[1] = (int) (WindCard.this.fact * xvec) + WindCard.this.xt;
										whys[1] = (int) (WindCard.this.fact * -yvec) + WindCard.this.yt;
										Sys.this.windViewerImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1],
												whys[1]);
										/*
										 * if ((i-antim)/3*3 == (i-antim) ) {
										 * off3Gg.drawLine(exes[0],whys[0],exes[1],whys[1]) ; }
										 */
									}
								}
							}

							// wing surface
							if (WindCard.this.viewflg == WALL_VIEW_SOLID) { // 3d geom
								exes[1] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2])
										+ WindCard.this.xt1;
								whys[1] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2])
										+ WindCard.this.yt1;
								exes[2] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2])
										+ WindCard.this.xt2;
								whys[2] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2])
										+ WindCard.this.yt2;
								for (i = 1; i <= WindCard.this.npt2 - 1; ++i) {
									exes[0] = exes[1];
									whys[0] = whys[1];
									exes[1] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2 - i])
											+ WindCard.this.xt1;
									whys[1] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2 - i])
											+ WindCard.this.yt1;
									exes[3] = exes[2];
									whys[3] = whys[2];
									exes[2] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2 - i])
											+ WindCard.this.xt2;
									whys[2] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2 - i])
											+ WindCard.this.yt2;
									Sys.this.windViewerImgBuffGraphContext.setColor(Color.red);
									Sys.this.windViewerImgBuffGraphContext.fillPolygon(exes, whys, 4);
									Sys.this.windViewerImgBuffGraphContext.setColor(Color.black);
									Sys.this.windViewerImgBuffGraphContext.drawLine(exes[1], whys[1], exes[2], whys[2]);
								}
							}

							if (WindCard.this.vfsd > .01) {
								for (j = WindCard.this.nln2 + 1; j <= WindCard.this.nlnc; ++j) { /* upper half */
									for (i = 1; i <= WindCard.this.nptc - 1; ++i) {
										exes[0] = (int) (WindCard.this.fact * WindCard.this.xpl[j][i])
												+ WindCard.this.xt;
										yprs = .1 * WindCard.this.xpl[j][i];
										yprs1 = .1 * WindCard.this.xpl[j][i + 1];
										whys[0] = (int) (WindCard.this.fact * (-WindCard.this.ypl[j][i] + yprs))
												+ WindCard.this.yt;
										slope = (WindCard.this.ypl[j][i + 1] - yprs1 - WindCard.this.ypl[j][i] + yprs)
												/ (WindCard.this.xpl[j][i + 1] - WindCard.this.xpl[j][i]);
										xvec = WindCard.this.xpl[j][i] + radvec / Math.sqrt(1.0 + slope * slope);
										yvec = WindCard.this.ypl[j][i] - yprs
												+ slope * (xvec - WindCard.this.xpl[j][i]);
										exes[1] = (int) (WindCard.this.fact * xvec) + WindCard.this.xt;
										whys[1] = (int) (WindCard.this.fact * -yvec) + WindCard.this.yt;
										if (WindCard.this.displ == 0) { /* MODS 21 JUL 99 */
											Sys.this.windViewerImgBuffGraphContext.setColor(col);
											exes[1] = (int) (WindCard.this.fact * WindCard.this.xpl[j][i + 1])
													+ WindCard.this.xt;
											yprs = .1 * WindCard.this.xpl[j][1 + 1];
											whys[1] = (int) (WindCard.this.fact * (-WindCard.this.ypl[j][i + 1] + yprs))
													+ WindCard.this.yt;
											Sys.this.windViewerImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1],
													whys[1]);
										}
										if (WindCard.this.displ == 2 && i / 3 * 3 == i) {
											Sys.this.windViewerImgBuffGraphContext.setColor(col); /* MODS 27 JUL 99 */
											for (n = 1; n <= 4; ++n) {
												if (i == 6 + (n - 1) * 9) {
													Sys.this.windViewerImgBuffGraphContext.setColor(Color.yellow);
												}
											}
											if (i / 9 * 9 == i) {
												Sys.this.windViewerImgBuffGraphContext.setColor(Color.white);
											}
											Sys.this.windViewerImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1],
													whys[1]);
										}
										if (WindCard.this.displ == 1
												&& (i - WindCard.this.antim) / 3 * 3 == i - WindCard.this.antim) {
											if (WindCard.this.ancol == -1) { /* MODS 27 JUL 99 */
												if ((i - WindCard.this.antim) / 6 * 6 == i - WindCard.this.antim) {
													Sys.this.windViewerImgBuffGraphContext.setColor(col);
												}
												if ((i - WindCard.this.antim) / 6 * 6 != i - WindCard.this.antim) {
													Sys.this.windViewerImgBuffGraphContext.setColor(Color.white);
												}
											}
											if (WindCard.this.ancol == 1) { /* MODS 27 JUL 99 */
												if ((i - WindCard.this.antim) / 6 * 6 == i - WindCard.this.antim) {
													Sys.this.windViewerImgBuffGraphContext.setColor(Color.white);
												}
												if ((i - WindCard.this.antim) / 6 * 6 != i - WindCard.this.antim) {
													Sys.this.windViewerImgBuffGraphContext.setColor(col);
												}
											}
											Sys.this.windViewerImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1],
													whys[1]);
										}
									}
								}
							}
							if (WindCard.this.pboflag > 0 && WindCard.this.pypl > 0.0) {
								Sys.this.windViewerImgBuffGraphContext.setColor(Color.magenta);
								yprs = .1 * WindCard.this.pxpl;
								Sys.this.windViewerImgBuffGraphContext.fillOval(
										(int) (WindCard.this.fact * WindCard.this.pxpl) + WindCard.this.xt,
										(int) (WindCard.this.fact * (-WindCard.this.pypl + yprs)) + WindCard.this.yt
												- 2,
										5, 5);
								Sys.this.windViewerImgBuffGraphContext.setColor(Color.white);
								exes[0] = (int) (WindCard.this.fact * (WindCard.this.pxpl + .1)) + WindCard.this.xt;
								whys[0] = (int) (WindCard.this.fact * (-WindCard.this.pypl + yprs)) + WindCard.this.yt;
								exes[1] = (int) (WindCard.this.fact * (WindCard.this.pxpl + .5)) + WindCard.this.xt;
								whys[1] = (int) (WindCard.this.fact * (-WindCard.this.pypl + yprs)) + WindCard.this.yt;
								exes[2] = (int) (WindCard.this.fact * (WindCard.this.pxpl + .5)) + WindCard.this.xt;
								whys[2] = (int) (WindCard.this.fact * (-WindCard.this.pypl - 50.)) + WindCard.this.yt;
								Sys.this.windViewerImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1], whys[1]);
								Sys.this.windViewerImgBuffGraphContext.drawLine(exes[1], whys[1], exes[2], whys[2]);
								if (WindCard.this.pboflag == PROBLE_SMOKE && WindCard.this.vfsd >= .01) { // smoke trail
									Sys.this.windViewerImgBuffGraphContext.setColor(Color.green);
									for (i = 1; i <= WindCard.this.nptc - 1; ++i) {
										exes[0] = (int) (WindCard.this.fact * WindCard.this.xpl[19][i])
												+ WindCard.this.xt;
										yprs = .1 * WindCard.this.xpl[19][i];
										yprs1 = .1 * WindCard.this.xpl[19][i + 1];
										whys[0] = (int) (WindCard.this.fact * (-WindCard.this.ypl[19][i] + yprs))
												+ WindCard.this.yt;
										slope = (WindCard.this.ypl[19][i + 1] - yprs1 - WindCard.this.ypl[19][i] + yprs)
												/ (WindCard.this.xpl[19][i + 1] - WindCard.this.xpl[19][i]);
										xvec = WindCard.this.xpl[19][i] + radvec / Math.sqrt(1.0 + slope * slope);
										yvec = WindCard.this.ypl[19][i] - yprs
												+ slope * (xvec - WindCard.this.xpl[19][i]);
										exes[1] = (int) (WindCard.this.fact * xvec) + WindCard.this.xt;
										whys[1] = (int) (WindCard.this.fact * -yvec) + WindCard.this.yt;
										Sys.this.windViewerImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1],
												whys[1]);
										/*
										 * if ((i-antim)/3*3 == (i-antim) ) {
										 * off3Gg.drawLine(exes[0],whys[0],exes[1],whys[1]) ; }
										 */
									}
								}
							}

							if (WindCard.this.viewflg == WALL_VIEW_TRANSPARENT) {
								// front foil
								Sys.this.windViewerImgBuffGraphContext.setColor(Color.white);
								exes[1] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2])
										+ WindCard.this.xt2;
								whys[1] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2])
										+ WindCard.this.yt2;
								exes[2] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2])
										+ WindCard.this.xt2;
								whys[2] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2])
										+ WindCard.this.yt2;
								for (i = 1; i <= WindCard.this.npt2 - 1; ++i) {
									exes[0] = exes[1];
									whys[0] = whys[1];
									exes[1] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2 - i])
											+ WindCard.this.xt2;
									whys[1] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2 - i])
											+ WindCard.this.yt2;
									exes[3] = exes[2];
									whys[3] = whys[2];
									exes[2] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2 + i])
											+ WindCard.this.xt2;
									whys[2] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2 + i])
											+ WindCard.this.yt2;
									camx[i] = (exes[1] + exes[2]) / 2;
									camy[i] = (whys[1] + whys[2]) / 2;
									Sys.this.windViewerImgBuffGraphContext.fillPolygon(exes, whys, 4);
								}
								// middle airfoil geometry
								Sys.this.windViewerImgBuffGraphContext.setColor(Color.white);
								exes[1] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2])
										+ WindCard.this.xt;
								whys[1] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2])
										+ WindCard.this.yt;
								exes[2] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2])
										+ WindCard.this.xt;
								whys[2] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2])
										+ WindCard.this.yt;
								for (i = 1; i <= WindCard.this.npt2 - 1; ++i) {
									exes[0] = exes[1];
									whys[0] = whys[1];
									exes[1] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2 - i])
											+ WindCard.this.xt;
									whys[1] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2 - i])
											+ WindCard.this.yt;
									exes[3] = exes[2];
									whys[3] = whys[2];
									exes[2] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2 + i])
											+ WindCard.this.xt;
									whys[2] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2 + i])
											+ WindCard.this.yt;
									camx[i] = (exes[1] + exes[2]) / 2;
									camy[i] = (whys[1] + whys[2]) / 2;
									if (WindCard.this.foiltype == FOILTYPE_PLATE) {
										Sys.this.windViewerImgBuffGraphContext.setColor(Color.yellow);
										Sys.this.windViewerImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1],
												whys[1]);
									} else {
										Sys.this.windViewerImgBuffGraphContext.setColor(Color.white);
										Sys.this.windViewerImgBuffGraphContext.fillPolygon(exes, whys, 4);
									}
								}
								// back foil
								Sys.this.windViewerImgBuffGraphContext.setColor(Color.white);
								exes[1] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2])
										+ WindCard.this.xt1;
								whys[1] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2])
										+ WindCard.this.yt1;
								exes[2] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2])
										+ WindCard.this.xt1;
								whys[2] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2])
										+ WindCard.this.yt1;
								for (i = 1; i <= WindCard.this.npt2 - 1; ++i) {
									exes[0] = exes[1];
									whys[0] = whys[1];
									exes[1] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2 - i])
											+ WindCard.this.xt1;
									whys[1] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2 - i])
											+ WindCard.this.yt1;
									exes[3] = exes[2];
									whys[3] = whys[2];
									exes[2] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2 + i])
											+ WindCard.this.xt1;
									whys[2] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2 + i])
											+ WindCard.this.yt1;
									camx[i] = (exes[1] + exes[2]) / 2;
									camy[i] = (whys[1] + whys[2]) / 2;
									Sys.this.windViewerImgBuffGraphContext.fillPolygon(exes, whys, 4);
								}
								// leading and trailing edge
								Sys.this.windViewerImgBuffGraphContext.setColor(Color.white);
								exes[1] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2])
										+ WindCard.this.xt1;
								whys[1] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2])
										+ WindCard.this.yt1;
								exes[2] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2])
										+ WindCard.this.xt2;
								whys[2] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2])
										+ WindCard.this.yt2;
								Sys.this.windViewerImgBuffGraphContext.drawLine(exes[1], whys[1], exes[2], whys[2]);
								exes[1] = (int) (WindCard.this.fact * WindCard.this.xpl[0][1]) + WindCard.this.xt1;
								whys[1] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][1]) + WindCard.this.yt1;
								exes[2] = (int) (WindCard.this.fact * WindCard.this.xpl[0][1]) + WindCard.this.xt2;
								whys[2] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][1]) + WindCard.this.yt2;
								Sys.this.windViewerImgBuffGraphContext.drawLine(exes[1], whys[1], exes[2], whys[2]);
								// put some info on the geometry
								if (WindCard.this.displ == 3) {
									if (WindCard.this.foiltype <= FOILTYPE_PLATE) {
										inmax = 1;
										for (n = 1; n <= WindCard.this.nptc; ++n) {
											if (WindCard.this.xpl[0][n] > WindCard.this.xpl[0][inmax]) {
												inmax = n;
											}
										}
										Sys.this.windViewerImgBuffGraphContext.setColor(Color.green);
										exes[0] = (int) (WindCard.this.fact * WindCard.this.xpl[0][inmax])
												+ WindCard.this.xt;
										whys[0] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][inmax])
												+ WindCard.this.yt;
										Sys.this.windViewerImgBuffGraphContext.drawLine(exes[0], whys[0], exes[0] - 250,
												whys[0]);
										Sys.this.windViewerImgBuffGraphContext.drawString("Reference", 30,
												whys[0] + 10);
										Sys.this.windViewerImgBuffGraphContext.drawString(Sys.ANGLE, exes[0] + 20,
												whys[0]);

										Sys.this.windViewerImgBuffGraphContext.setColor(Color.cyan);
										exes[1] = (int) (WindCard.this.fact * (WindCard.this.xpl[0][inmax]
												- 4.0 * Math.cos(WindCard.this.convdr * WindCard.this.alfval)))
												+ WindCard.this.xt;
										whys[1] = (int) (WindCard.this.fact * (-WindCard.this.ypl[0][inmax]
												- 4.0 * Math.sin(WindCard.this.convdr * WindCard.this.alfval)))
												+ WindCard.this.yt;
										Sys.this.windViewerImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1],
												whys[1]);
										Sys.this.windViewerImgBuffGraphContext.drawString("Chord Line", exes[0] + 20,
												whys[0] + 20);

										Sys.this.windViewerImgBuffGraphContext.setColor(Color.red);
										Sys.this.windViewerImgBuffGraphContext.drawLine(exes[1], whys[1], camx[5],
												camy[5]);
										for (i = 7; i <= WindCard.this.npt2 - 6; i = i + 2) {
											Sys.this.windViewerImgBuffGraphContext.drawLine(camx[i], camy[i],
													camx[i + 1], camy[i + 1]);
										}
										Sys.this.windViewerImgBuffGraphContext.drawString("Mean Camber Line",
												exes[0] - 70, whys[1] - 10);
									}
									if (WindCard.this.foiltype >= FOILTYPE_CYLINDER) {
										Sys.this.windViewerImgBuffGraphContext.setColor(Color.red);
										exes[0] = (int) (WindCard.this.fact * WindCard.this.xpl[0][1])
												+ WindCard.this.xt;
										whys[0] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][1])
												+ WindCard.this.yt;
										exes[1] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2])
												+ WindCard.this.xt;
										whys[1] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2])
												+ WindCard.this.yt;
										Sys.this.windViewerImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1],
												whys[1]);
										Sys.this.windViewerImgBuffGraphContext.drawString(Sys.DIAMETER, exes[0] + 20,
												whys[0] + 20);
									}

									Sys.this.windViewerImgBuffGraphContext.setColor(Color.green);
									Sys.this.windViewerImgBuffGraphContext.drawString(Sys.FLOW, 30, 145);
									Sys.this.windViewerImgBuffGraphContext.drawLine(30, 152, 60, 152);
									exes[0] = 60;
									exes[1] = 60;
									exes[2] = 70;
									whys[0] = 157;
									whys[1] = 147;
									whys[2] = 152;
									Sys.this.windViewerImgBuffGraphContext.fillPolygon(exes, whys, 3);
								}
								// spin the cylinder and ball
								if (WindCard.this.foiltype >= FOILTYPE_CYLINDER) {
									exes[0] = (int) (WindCard.this.fact * (.5
											* (WindCard.this.xpl[0][1] + WindCard.this.xpl[0][WindCard.this.npt2])
											+ WindCard.this.rval
													* Math.cos(WindCard.this.convdr * (WindCard.this.plthg[1] + 180.))))
											+ WindCard.this.xt;
									whys[0] = (int) (WindCard.this.fact * (-WindCard.this.ypl[0][1] + WindCard.this.rval
											* Math.sin(WindCard.this.convdr * (WindCard.this.plthg[1] + 180.))))
											+ WindCard.this.yt;
									exes[1] = (int) (WindCard.this.fact
											* (.5 * (WindCard.this.xpl[0][1] + WindCard.this.xpl[0][WindCard.this.npt2])
													+ WindCard.this.rval
															* Math.cos(WindCard.this.convdr * WindCard.this.plthg[1])))
											+ WindCard.this.xt;
									whys[1] = (int) (WindCard.this.fact * (-WindCard.this.ypl[0][1] + WindCard.this.rval
											* Math.sin(WindCard.this.convdr * WindCard.this.plthg[1])))
											+ WindCard.this.yt;
									Sys.this.windViewerImgBuffGraphContext.setColor(Color.red);
									Sys.this.windViewerImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1], whys[1]);
								}
							}
							if (WindCard.this.viewflg == WALL_VIEW_SOLID) {
								// front foil
								Sys.this.windViewerImgBuffGraphContext.setColor(Color.white);
								exes[1] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2])
										+ WindCard.this.xt2;
								whys[1] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2])
										+ WindCard.this.yt2;
								exes[2] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2])
										+ WindCard.this.xt2;
								whys[2] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2])
										+ WindCard.this.yt2;
								for (i = 1; i <= WindCard.this.npt2 - 1; ++i) {
									exes[0] = exes[1];
									whys[0] = whys[1];
									exes[1] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2 - i])
											+ WindCard.this.xt2;
									whys[1] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2 - i])
											+ WindCard.this.yt2;
									exes[3] = exes[2];
									whys[3] = whys[2];
									exes[2] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2 + i])
											+ WindCard.this.xt2;
									whys[2] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][WindCard.this.npt2 + i])
											+ WindCard.this.yt2;
									camx[i] = (exes[1] + exes[2]) / 2;
									camy[i] = (whys[1] + whys[2]) / 2;
									Sys.this.windViewerImgBuffGraphContext.fillPolygon(exes, whys, 4);
								}
								// put some info on the geometry
								if (WindCard.this.displ == 3) {
									Sys.this.windViewerImgBuffGraphContext.setColor(Color.green);
									exes[1] = (int) (WindCard.this.fact * WindCard.this.xpl[0][1]) + WindCard.this.xt1
											+ 20;
									whys[1] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][1]) + WindCard.this.yt1;
									exes[2] = (int) (WindCard.this.fact * WindCard.this.xpl[0][1]) + WindCard.this.xt2
											+ 20;
									whys[2] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][1]) + WindCard.this.yt2;
									Sys.this.windViewerImgBuffGraphContext.drawLine(exes[1], whys[1], exes[2], whys[2]);
									Sys.this.windViewerImgBuffGraphContext.drawString("Span", exes[2] + 10,
											whys[2] + 10);

									exes[1] = (int) (WindCard.this.fact * WindCard.this.xpl[0][1]) + WindCard.this.xt2;
									whys[1] = (int) (WindCard.this.fact * -WindCard.this.ypl[0][1]) + WindCard.this.yt2
											+ 15;
									exes[2] = (int) (WindCard.this.fact * WindCard.this.xpl[0][WindCard.this.npt2])
											+ WindCard.this.xt2;
									whys[2] = whys[1];
									Sys.this.windViewerImgBuffGraphContext.drawLine(exes[1], whys[1], exes[2], whys[2]);
									if (WindCard.this.foiltype <= FOILTYPE_PLATE) {
										Sys.this.windViewerImgBuffGraphContext.drawString(Sys.CHORD2, exes[2] + 10,
												whys[2] + 15);
									}
									if (WindCard.this.foiltype >= FOILTYPE_CYLINDER) {
										Sys.this.windViewerImgBuffGraphContext.drawString(Sys.DIAMETER, exes[2] + 10,
												whys[2] + 15);
									}

									Sys.this.windViewerImgBuffGraphContext.drawString(Sys.FLOW, 40, 75);
									Sys.this.windViewerImgBuffGraphContext.drawLine(30, 82, 60, 82);
									exes[0] = 60;
									exes[1] = 60;
									exes[2] = 70;
									whys[0] = 87;
									whys[1] = 77;
									whys[2] = 82;
									Sys.this.windViewerImgBuffGraphContext.fillPolygon(exes, whys, 3);
								}
								// spin the cylinder and ball
								if (WindCard.this.foiltype >= FOILTYPE_CYLINDER) {
									exes[0] = (int) (WindCard.this.fact * (.5
											* (WindCard.this.xpl[0][1] + WindCard.this.xpl[0][WindCard.this.npt2])
											+ WindCard.this.rval
													* Math.cos(WindCard.this.convdr * (WindCard.this.plthg[1] + 180.))))
											+ WindCard.this.xt2;
									whys[0] = (int) (WindCard.this.fact * (-WindCard.this.ypl[0][1] + WindCard.this.rval
											* Math.sin(WindCard.this.convdr * (WindCard.this.plthg[1] + 180.))))
											+ WindCard.this.yt2;
									exes[1] = (int) (WindCard.this.fact
											* (.5 * (WindCard.this.xpl[0][1] + WindCard.this.xpl[0][WindCard.this.npt2])
													+ WindCard.this.rval
															* Math.cos(WindCard.this.convdr * WindCard.this.plthg[1])))
											+ WindCard.this.xt2;
									whys[1] = (int) (WindCard.this.fact * (-WindCard.this.ypl[0][1] + WindCard.this.rval
											* Math.sin(WindCard.this.convdr * WindCard.this.plthg[1])))
											+ WindCard.this.yt2;
									Sys.this.windViewerImgBuffGraphContext.setColor(Color.red);
									Sys.this.windViewerImgBuffGraphContext.drawLine(exes[0], whys[0], exes[1], whys[1]);
								}
							}
						}

						// Labels

						Sys.this.windViewerImgBuffGraphContext.setColor(Color.lightGray);
						Sys.this.windViewerImgBuffGraphContext.fillRect(0, 295, 500, 50);
						Sys.this.windViewerImgBuffGraphContext.setColor(Color.white);
						if (WindCard.this.viewflg == WALL_VIEW_TRANSPARENT) {
							Sys.this.windViewerImgBuffGraphContext.setColor(Color.yellow);
						}
						Sys.this.windViewerImgBuffGraphContext.fillRect(72, 302, 160, 20);
						Sys.this.windViewerImgBuffGraphContext.setColor(Color.black);
						Sys.this.windViewerImgBuffGraphContext.drawString("W-Transparent Surface", 80, 317);

						Sys.this.windViewerImgBuffGraphContext.setColor(Color.white);
						if (WindCard.this.viewflg == WALL_VIEW_SOLID) {
							Sys.this.windViewerImgBuffGraphContext.setColor(Color.yellow);
						}
						Sys.this.windViewerImgBuffGraphContext.fillRect(240, 302, 150, 20);
						Sys.this.windViewerImgBuffGraphContext.setColor(Color.black);
						Sys.this.windViewerImgBuffGraphContext.drawString("Solid Surface", 268, 317);

						g.drawImage(Sys.this.windViewerImageBuffer, 0, 0, this);
					}
				}

				/**
				 * (non-Javadoc).
				 *
				 * @see java.lang.Runnable#run()
				 */
				@Override
				public void run() {
					int timer;

					timer = 100;
					while (true) {
						++WindCard.this.antim;
						try {
							Thread.sleep(timer);
						} catch (final InterruptedException e) {
						}
						WindCard.this.view.repaint();
						if (WindCard.this.antim == 3) {
							WindCard.this.antim = 0;
							WindCard.this.ancol = -WindCard.this.ancol; /* MODS 27 JUL 99 */
						}
						timer = 135 - (int) (.227 * WindCard.this.vfsd / WindCard.this.vconv);
						// make the ball spin
						if (WindCard.this.foiltype >= FOILTYPE_CYLINDER) {
							WindCard.this.plthg[1] = WindCard.this.plthg[1]
									+ WindCard.this.spin * WindCard.this.spindr * 5.;
							if (WindCard.this.plthg[1] < -360.0) {
								WindCard.this.plthg[1] = WindCard.this.plthg[1] + 360.0;
							}
							if (WindCard.this.plthg[1] > 360.0) {
								WindCard.this.plthg[1] = WindCard.this.plthg[1] - 360.0;
							}
						}
					}
				}

				/** Start. */
				public void start() {
					if (this.runner == null) {
						this.runner = new Thread(this);
						this.runner.start();
					}
					Sys.logger.info("Wind WindViewwer Started");
					WindCard.this.antim = 0; /* MODS 21 JUL 99 */
					WindCard.this.ancol = 1; /* MODS 27 JUL 99 */
				}

				/**
				 * (non-Javadoc).
				 *
				 * @param g
				 *            the g
				 * @see java.awt.Canvas#update(java.awt.Graphics)
				 */
				@Override
				public void update(Graphics g) {
					WindCard.this.view.paint(g);
				}
			} // end Viewer

			/** The Constant serialVersionUID. */
			private static final long serialVersionUID = 1L;

			/** The endy. */
			double begx;

			/** The endx. */
			double endx;

			/** The begy. */
			double begy;

			/** The endy. */
			double endy;

			/** The arcor. */
			int calcrange;

			/** The arcor. */
			int arcor;

			/** The thkmx. */
			double camn;

			/** The thkmn. */
			double thkmn;

			/** The camx. */
			double camx;

			/** The thkmx. */
			double thkmx;

			/** The spnold. */
			double chord;

			/** The span. */
			double span;

			/** The aspr. */
			double aspr;

			/** The arold. */
			double arold;

			/** The chrdold. */
			double chrdold;

			/** The spnold. */
			double spnold;

			/** The armx. */
			double chrdmn;

			/** The armn. */
			double armn;

			/** The chrdmx. */
			double chrdmx;

			/** The armx. */
			double armx;

			/** The convdr. */
			double convdr = Sys.PI / 180.;

			/** The conw. */
			Conw conw;

			/** The dcam. */
			double dcam[] = new double[40];

			/** The dchrd. */
			double dchrd[] = new double[40];

			/** The xflow. */
			double deltb;

			/** The xflow. */
			double xflow; /* MODS 20 Jul 99 */

			/** The radius. */
			double delx;

			/** The delt. */
			double delt;

			/** The vfsd. */
			double vfsd;

			/** The spin. */
			double spin;

			/** The spindr. */
			double spindr;

			/** The yoff. */
			double yoff;

			/** The radius. */
			double radius;

			/** The dftp. */
			int dftp[] = new int[40];

			/** The sldloc. */
			int displ;

			/** The viewflg. */
			int viewflg;

			/** The dispp. */
			int dispp;

			/** The dout. */
			int dout;

			/** The antim. */
			int antim;

			/** The ancol. */
			int ancol;

			/** The sldloc. */
			int sldloc;

			/** The dlunits. */
			int dlunits[] = new int[40];

			/** The dspan. */
			double dspan[] = new double[40];

			/** The dthk. */
			double dthk[] = new double[40];

			/** The chrdfac. */
			/* plot & probe data */
			double fact;

			/** The xpval. */
			double xpval;

			/** The ypval. */
			double ypval;

			/** The pbval. */
			double pbval;

			/** The factp. */
			double factp;

			/** The chrdfac. */
			double chrdfac;

			/** The planet. */
			int foiltype;

			/** The flflag. */
			int flflag;

			/** The lunits. */
			int lunits;

			/** The lftout. */
			int lftout;

			/** The planet. */
			int planet;

			/** The presm. */
			double g0;

			/** The q 0. */
			double q0;

			/** The ps 0. */
			double ps0;

			/** The pt 0. */
			double pt0;

			/** The ts 0. */
			double ts0;

			/** The rho. */
			double rho;

			/** The rlhum. */
			double rlhum;

			/** The temf. */
			double temf;

			/** The presm. */
			double presm;

			/** The nummod. */
			int inptopt;

			/** The outopt. */
			int outopt;

			/** The iprint. */
			int iprint;

			/** The nummod. */
			int nummod;

			/** The inw. */
			Inw inw;

			/** The labyu. */
			String labx, labxu, laby, labyu;

			/** The layperf. */
			CardLayout layin, layout, layplt, layperf;

			/** The tem. */
			double leg;

			/** The teg. */
			double teg;

			/** The lem. */
			double lem;

			/** The tem. */
			double tem;

			/** The nond. */
			int lflag;

			/** The gflag. */
			int gflag;

			/** The plscale. */
			int plscale;

			/** The nond. */
			int nond;

			/** The ntr. */
			int lines;

			/** The nord. */
			int nord;

			/** The nabs. */
			int nabs;

			/** The ntr. */
			int ntr;

			/** The vxdir. */
			double lxm;

			/** The lym. */
			double lym;

			/** The lxmt. */
			double lxmt;

			/** The lymt. */
			double lymt;

			/** The vxdir. */
			double vxdir;/* MOD 20 Jul */

			/** The lygtc. */
			double lyg;

			/** The lrg. */
			double lrg;

			/** The lthg. */
			double lthg;

			/** The lxgt. */
			double lxgt;

			/** The lygt. */
			double lygt;

			/** The lrgt. */
			double lrgt;

			/** The lthgt. */
			double lthgt;

			/** The lxgtc. */
			double lxgtc;

			/** The lygtc. */
			double lygtc;

			/** The anflag. */
			int nptc;

			/** The npt 2. */
			int npt2;

			/** The nlnc. */
			int nlnc;

			/** The nln 2. */
			int nln2;

			/** The rdflag. */
			int rdflag;

			/** The browflag. */
			int browflag;

			/** The probflag. */
			int probflag;

			/** The anflag. */
			int anflag;

			/** The outerparent. */
			Sys outerparent;

			/** The ouw. */
			Ouw ouw;

			/** The ytp. */
			int pboflag;

			/** The xt. */
			int xt;

			/** The yt. */
			int yt;

			/** The ntikx. */
			int ntikx;

			/** The ntiky. */
			int ntiky;

			/** The npt. */
			int npt;

			/** The xtp. */
			int xtp;

			/** The ytp. */
			int ytp;

			/** The fmaxb. */
			double pconv;

			/** The pmax. */
			double pmax;

			/** The pmin. */
			double pmin;

			/** The lconv. */
			double lconv;

			/** The rconv. */
			double rconv;

			/** The fconv. */
			double fconv;

			/** The fmax. */
			double fmax;

			/** The fmaxb. */
			double fmaxb;

			/** The ticonv. */
			double piconv;

			/** The ticonv. */
			double ticonv;

			/** The pid 2. */
			double pid2 = Sys.PI / 2.0;

			/** The plp. */
			double plp[] = new double[80];

			/** The plthg. */
			double plthg[] = new double[2];

			/** The pltx. */
			double pltx[][] = new double[3][40];

			/** The plty. */
			double plty[][] = new double[3][40];

			/** The plv. */
			double plv[] = new double[80];

			/** The dato. */
			int pointSet;

			/** The datp. */
			int datp;

			/** The dato. */
			int dato;

			/** The pypl. */
			double prg;

			/** The pthg. */
			double pthg;

			/** The pxg. */
			double pxg;

			/** The pyg. */
			double pyg;

			/** The pxm. */
			double pxm;

			/** The pym. */
			double pym;

			/** The pxpl. */
			double pxpl;

			/** The pypl. */
			double pypl;

			/** The tsmax. */
			double psin;

			/** The tsin. */
			double tsin;

			/** The psmin. */
			double psmin;

			/** The psmax. */
			double psmax;

			/** The tsmin. */
			double tsmin;

			/** The tsmax. */
			double tsmax;

			/** The tsmx. */
			double psmn;

			/** The psmx. */
			double psmx;

			/** The tsmn. */
			double tsmn;

			/** The tsmx. */
			double tsmx;

			/** The spinmx. */
			double radmn;

			/** The spinmn. */
			double spinmn;

			/** The radmx. */
			double radmx;

			/** The spinmx. */
			double spinmx;

			/** The rg. */
			double rg[][] = new double[50][80];

			/** The clift. */
			double rval;

			/** The ycval. */
			double ycval;

			/** The xcval. */
			double xcval;

			/** The gamval. */
			double gamval;

			/** The alfval. */
			double alfval;

			/** The thkval. */
			double thkval;

			/** The camval. */
			double camval;

			/** The chrd. */
			double chrd;

			/** The clift. */
			double clift;

			/** The solvew. */
			SolveWind solvew;

			/** The stfact. */
			double stfact;

			/** The thg. */
			double thg[][] = new double[50][80];

			/** The caminpt. */
			double thkinpt;

			/** The caminpt. */
			double caminpt; /* MODS 10 Sep 99 */

			/** The armin. */
			double usq;

			/** The vsq. */
			double vsq;

			/** The alt. */
			double alt;

			/** The altmax. */
			double altmax;

			/** The area. */
			double area;

			/** The armax. */
			double armax;

			/** The armin. */
			double armin;

			/** The vmax. */
			double vconv;

			/** The vmax. */
			double vmax;

			/** The angr. */
			double vel;

			/** The pres. */
			double pres;

			/** The lift. */
			double lift;

			/** The side. */
			double side;

			/** The omega. */
			double omega;

			/** The radcrv. */
			double radcrv;

			/** The relsy. */
			double relsy;

			/** The angr. */
			double angr;

			/** The view. */
			WindViewer view;

			/** The angmx. */
			/* units data */
			double vmn;

			/** The almn. */
			double almn;

			/** The angmn. */
			double angmn;

			/** The vmx. */
			double vmx;

			/** The almx. */
			double almx;

			/** The angmx. */
			double angmx;

			/** The xg. */
			double xg[][] = new double[50][80];

			/** The xgc. */
			double xgc[][] = new double[50][80];

			/** The xm. */
			double xm[][] = new double[50][80];

			/** The xpl. */
			double xpl[][] = new double[50][80];

			/** The xplg. */
			double xplg[][] = new double[50][80];

			/** The spanfac. */
			int xt1;

			/** The yt 1. */
			int yt1;

			/** The xt 2. */
			int xt2;

			/** The yt 2. */
			int yt2;

			/** The spanfac. */
			int spanfac;

			/** The yg. */
			double yg[][] = new double[50][80];

			/** The ygc. */
			double ygc[][] = new double[50][80];

			/** The ym. */
			double ym[][] = new double[50][80];

			/** The ypl. */
			double ypl[][] = new double[50][80];

			/** The yplg. */
			double yplg[][] = new double[50][80];

			/**
			 * Instantiates a new wsys.
			 *
			 * @param target
			 *            the target
			 */
			WindCard(Sys target) {
				Sys.logger.info("Create Wind Card");
				this.setName("Wind Card");
				this.outerparent = target;
				this.setLayout(new GridLayout(2, 2, 5, 5));

				this.solvew = new SolveWind();
				this.solvew.setDefaults();

				this.view = new WindViewer(this.outerparent);
				this.conw = new Conw(this.outerparent);
				this.inw = new Inw(this.outerparent);
				this.ouw = new Ouw(this.outerparent);

				this.add(this.view);
				this.add(this.ouw);
				this.add(this.inw);
				this.add(this.conw);

				this.solvew.getFreeStream();
				this.computeFlow();
				this.view.start();
				this.ouw.plotter.start();
			}

			/** Compute flow. */
			public void computeFlow() {

				this.solvew.getFreeStream();

				this.solvew.getCirculation(); /* get circulation */
				this.solvew.getGometry(); /* get geometry */
				this.solvew.genFlow();

				this.solvew.getProbe();

				this.loadOut();

				this.ouw.plotter.loadPlot();
			}

			/** Load input. */
			public void loadInput() { // load the input panels
				int i3, i4, i5, i6;
				double v3, v4, v5, v6;
				float fl3, fl4, fl5, fl6;
				String outarea, outlngth;

				outlngth = " ft";
				if (this.lunits == UNITS_METERIC) {
					outlngth = " m";
				}
				outarea = " ft^2";
				if (this.lunits == UNITS_METERIC) {
					outarea = " m^2";
				}

				// dimensional
				if (this.lunits == UNITS_ENGLISH) {
					this.inw.flt.upr.inl.l1.setText("Speed-mph");
					this.inw.flt.upr.inl.l2.setText("Pressure-psf");
					// inw.flt.upr.inl.l4.setText("Temperature - R") ;
					this.inw.cyl.inl.l2.setText(Sys.RADIUS_FT);
					this.inw.cyl.inl.l3.setText("Span ft");
				}
				if (this.lunits == UNITS_METERIC) {
					this.inw.flt.upr.inl.l1.setText("Speed-km/h");
					this.inw.flt.upr.inl.l2.setText("Pressure-kPa");
					// inw.flt.upr.inl.l4.setText("Temperature - K") ;
					this.inw.cyl.inl.l2.setText("Radius m");
					this.inw.cyl.inl.l3.setText("Span m");
				}
				v4 = this.vfsd;
				this.vmn = 0.0;
				this.vmx = this.vmax;
				v5 = this.psin;
				this.psmn = this.psmin * this.piconv;
				this.psmx = this.psmax * this.piconv;
				v6 = this.radius;
				this.radmn = .05 * this.lconv;
				this.radmx = 5.0 * this.lconv;
				this.aspr = this.span / this.chord;
				this.area = this.span * this.chord;
				this.spanfac = (int) (4.0 * 50.0 * .3535);

				fl4 = Sys.filter3(v4);
				fl5 = Sys.filter3(v5);
				fl6 = (float) v6;

				this.inw.flt.upr.inl.f1.setText(String.valueOf(fl4));
				this.inw.flt.upr.inl.f2.setText(String.valueOf(fl5));
				this.inw.cyl.inl.f2.setText(String.valueOf(fl6));

				i4 = (int) ((v4 - this.vmn) / (this.vmx - this.vmn) * 1000.);
				i5 = (int) ((v5 - this.psmn) / (this.psmx - this.psmn) * 1000.);
				i6 = (int) ((v6 - this.radmn) / (this.radmx - this.radmn) * 1000.);

				this.inw.flt.upr.inr.s1.setValue(i4);
				this.inw.flt.upr.inr.s2.setValue(i5);
				this.inw.cyl.inr.s2.setValue(i6);
				/*
				 * v5 = tsin ; tsmn = tsmin*ticonv ; tsmx = tsmax*ticonv ; fl5 = filter0(v5) ;
				 * inw.flt.upr.inl.f4.setText(String.valueOf(fl5)) ; i5 = (int) (((v5 -
				 * tsmn)/(tsmx-tsmn))*1000.) ; inw.flt.upr.inr.s4.setValue(i5) ;
				 */
				// non-dimensional
				v3 = this.alfval;
				v4 = this.spin * 60.0;

				fl3 = (float) v3;
				fl4 = (float) v4;

				this.inw.flt.upr.inl.f3.setText(String.valueOf(fl3));
				this.inw.cyl.inl.f1.setText(String.valueOf(fl4));

				i3 = (int) ((v3 - this.angmn) / (this.angmx - this.angmn) * 1000.);
				i4 = (int) ((v4 - this.spinmn) / (this.spinmx - this.spinmn) * 1000.);

				this.inw.flt.upr.inr.s3.setValue(i3);
				this.inw.cyl.inr.s1.setValue(i4);

				this.conw.dwn.o2.setText(String.valueOf(Sys.filter3(this.camval * 25.0)));
				this.conw.dwn.o4.setText(String.valueOf(Sys.filter3(this.thkval * 25.0)));
				this.conw.dwn.o5.setText(String.valueOf(Sys.filter3(this.aspr)));
				this.conw.dwn.o1.setText(Sys.filter3(this.chord) + outlngth);
				this.conw.dwn.o3.setText(Sys.filter3(this.span) + outlngth);
				this.conw.dwn.o6.setText(Sys.filter3(this.area) + outarea);

				// conw.dwn.o4.setText(String.valueOf(filter3(clift))) ;
				this.computeFlow();
				return;
			}

			/** Load out. */
			public void loadOut() { // output routine
				String outfor, outden, outpres;

				outfor = " lbs";
				if (this.lunits == UNITS_METERIC) {
					outfor = " N";
				}
				outden = " slug/ft^3";
				if (this.lunits == UNITS_METERIC) {
					outden = " kg/m^3";
				}
				outpres = Sys.LBS_FT_2;
				if (this.lunits == UNITS_METERIC) {
					outpres = Sys.K_PA;
				}
				if (this.lunits == UNITS_METERIC) {
				}
				this.area = this.span * this.chord;

				if (this.foiltype <= FOILTYPE_PLATE) { // mapped airfoil
					// stall model
					this.stfact = 1.0;
					if (this.anflag == 1) {
						if (this.alfval > 10.0) {
							this.stfact = .5 + .1 * this.alfval - .005 * this.alfval * this.alfval;
						}
						if (this.alfval < -10.0) {
							this.stfact = .5 - .1 * this.alfval - .005 * this.alfval * this.alfval;
						}
						this.clift = this.clift * this.stfact;
					}

					if (this.arcor == 1) { // correction for low aspect ratio
						this.clift = this.clift / (1.0 + this.clift / (3.14159 * this.aspr));
					}
					// conw.dwn.o4.setText(String.valueOf(filter3(clift))) ;

					if (this.lftout == 0) {
						this.lift = this.clift * this.q0 * this.area / this.lconv / this.lconv; /* lift in lbs */
						this.lift = this.lift * this.fconv;
						if (Math.abs(this.lift) <= 10.0) {
							this.conw.dwn.o10.setText(Sys.filter3(this.lift) + outfor);
						}
						if (Math.abs(this.lift) > 10.0) {
							this.conw.dwn.o10.setText(Sys.filter0(this.lift) + outfor);
						}
					}
				}

				switch (this.lunits) {
				case 0: { /* English */
					englishUnits(outden, outpres);
					break;
				}
				case 1: { /* Metric */
					metricUnits(outden, outpres);
					break;
				}
				}

				return;
			}

			/**
			 * Metric units.
			 *
			 * @param outden
			 *            the outden
			 * @param outpres
			 *            the outpres
			 */
			private void metricUnits(String outden, String outpres) {
				this.conw.dwn.o9.setText(Sys.filter3(this.rho * 515.4) + outden);
				this.conw.dwn.o7.setText(Sys.filter3(this.q0 * .04787) + outpres);
				this.conw.dwn.o8.setText(Sys.filter3(this.pt0 * .04787) + outpres);
			}

			/**
			 * English units.
			 *
			 * @param outden
			 *            the outden
			 * @param outpres
			 *            the outpres
			 */
			private void englishUnits(String outden, String outpres) {
				this.conw.dwn.o9.setText(Sys.filter5(this.rho) + outden);
				this.conw.dwn.o7.setText(Sys.filter3(this.q0) + outpres);
				this.conw.dwn.o8.setText(Sys.filter3(this.pt0) + outpres);
			}

			/** Load probe. */
			public void loadProbe() { // probe output routine

				this.pbval = 0.0;
				if (this.pboflag == PROBE_VELOCITY) {
					this.pbval = this.vel * this.vfsd; // velocity
				}
				if (this.pboflag == PROBE_PRESSURE) {
					this.pbval = (this.ps0 + this.pres * this.q0) / 2116.217 * this.pconv; // pressure
				}

				this.conw.probe.r.l2.repaint();
				return;
			}

			/** Sets the units. */
			public void setUnits() { // Switching Units
				double ovs, chords, spans, aros, chos, spos, rads;
				double alts, ares, pss, tss;

				alts = this.alt / this.lconv;
				chords = this.chord / this.lconv;
				spans = this.span / this.lconv;
				ares = this.area / this.lconv / this.lconv;
				aros = this.arold / this.lconv / this.lconv;
				chos = this.chrdold / this.lconv;
				spos = this.spnold / this.lconv;
				ovs = this.vfsd / this.vconv;
				rads = this.radius / this.lconv;
				pss = this.psin / this.piconv;
				tss = this.tsin / this.ticonv;

				switch (this.lunits) {
				case 0: { /* English */
					englishUnits();
					break;
				}
				case 1: { /* Metric */
					metricUnits();
					break;
				}
				}

				this.psin = pss * this.piconv;
				this.tsin = tss * this.ticonv;
				this.alt = alts * this.lconv;
				this.chord = chords * this.lconv;
				this.span = spans * this.lconv;
				this.area = ares * this.lconv * this.lconv;
				this.arold = aros * this.lconv * this.lconv;
				this.chrdold = chos * this.lconv;
				this.spnold = spos * this.lconv;
				this.vfsd = ovs * this.vconv;
				this.radius = rads * this.lconv;

				return;
			}

			/**
			 * Metric units.
			 */
			private void metricUnits() {
				this.lconv = .3048; /* meters */
				this.vconv = 1.097;
				this.vmax = 400.; /* km/hr */
				if (this.planet == 2) {
					this.vmax = 80.;
				}
				this.fconv = 4.448;
				this.fmax = 500000.;
				this.fmaxb = 2.5; /* newtons */
				this.piconv = .04788; /* kilo-pascals */
				this.ticonv = .5555555;
				this.pconv = 101.325; /* kilo-pascals */
			}

			/**
			 * English units.
			 */
			private void englishUnits() {
				this.lconv = 1.; /* feet */
				this.vconv = .6818;
				this.vmax = 250.; /* mph */
				if (this.planet == 2) {
					this.vmax = 50.;
				}
				this.fconv = 1.0;
				this.fmax = 100000.;
				this.fmaxb = .5; /* pounds */
				this.piconv = 1.0; /* lb/sq in */
				this.ticonv = 1.0;
				this.pconv = 14.696; /* lb/sq in */
			}

		} // end of Wsys

		/** The Constant FIRST_CARD. */
		private static final String FIRST_CARD = "first";

		/** The Constant FOURTH_CARD. */
		private static final String FOURTH_CARD = "fourth";

		/** The Constant SECOND_CARD. */
		private static final String SECOND_CARD = "second";

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/** The Constant THIRD_CARD. */
		private static final String THIRD_CARD = "third";

		/** The dsys. */
		DesignCard designCard;

		/** The titl. */
		MainMenu mainMenu;

		/** The outerparent. */
		Sys outerparent;

		/** The panels. */
		private final List<Panel> panels = new ArrayList<Panel>();

		/** The psys. */
		ProcessCard processCard;

		/** The wsys. */
		WindCard windCard;

		/**
		 * Instantiates a new pan.
		 *
		 * @param target
		 *            the target
		 */
		MainPanel(Sys target) {
			this.outerparent = target;
			final CardLayout cl = new CardLayout();

			Sys.this.setMainPanelLayout(cl);
			this.setLayout(cl);
			if (this.getLayout() != Sys.this.getMainPanelLayout()) {
				Sys.logger.info("NOT BE HERE");
			}

			this.mainMenu = new MainMenu(this.outerparent);
			this.panels.add(this.mainMenu);

			this.designCard = new DesignCard(this.outerparent);
			this.panels.add(this.designCard);

			this.windCard = new WindCard(this.outerparent);
			this.panels.add(this.windCard);

			this.processCard = new ProcessCard(this.outerparent);
			this.panels.add(this.processCard);

			this.add(MainPanel.FIRST_CARD, this.mainMenu);
			this.add(MainPanel.SECOND_CARD, this.designCard);
			this.add(MainPanel.THIRD_CARD, this.windCard);
			this.add(MainPanel.FOURTH_CARD, this.processCard);

		}

		/** Status panels. */
		public void statusPanels() {
			int i = 0;
			for (final Panel p : this.panels) {
				Sys.logger.info("(" + i++ + ") " + p.getName() + ": isVisible = " + p.isVisible());
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.
		 * ComponentEvent)
		 */
		@Override
		public void componentResized(ComponentEvent e) {
			logger.info("W: " + e.getComponent().getWidth() + " H: " + e.getComponent().getHeight());

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.
		 * ComponentEvent)
		 */
		@Override
		public void componentMoved(ComponentEvent e) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentShown(java.awt.event.
		 * ComponentEvent)
		 */
		@Override
		public void componentShown(ComponentEvent e) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.
		 * ComponentEvent)
		 */
		@Override
		public void componentHidden(ComponentEvent e) {
			// TODO Auto-generated method stub

		}

	} // end of Pan

	/** The Constant PROBE_OFF. */
	static final int PROBE_OFF = 0;

	/** The Constant PROBE_VELOCITY. */
	static final int PROBE_VELOCITY = 1;
	
	/** The Constant PROBE_PRESSURE. */
	static final int PROBE_PRESSURE = 2;
	
	/** The Constant PROBLE_SMOKE. */
	static final int PROBLE_SMOKE = 3;

	/** The Constant UNITS_ENGLISH. */
	static final int UNITS_ENGLISH = 0;
	
	/** The Constant UNITS_METERIC. */
	static final int UNITS_METERIC = 1;

	/** The Constant FOILTYPE_JUOKOWSKI. */
	static final int FOILTYPE_JUOKOWSKI = 1;

	/** The Constant FOILTYPE_ELLIPTICAL. */
	static final int FOILTYPE_ELLIPTICAL = 2;

	/** The Constant FOILTYPE_PLATE. */
	static final int FOILTYPE_PLATE = 3;

	/** The Constant FOILTYPE_CYLINDER. */
	static final int FOILTYPE_CYLINDER = 4;

	/** The Constant FOILTYPE_BALL. */
	static final int FOILTYPE_BALL = 5;

	/** The Constant PLANET_EARTH. */
	static final int PLANET_EARTH = 0;

	/** The Constant PLANET_MARS. */
	static final int PLANET_MARS = 1;

	/** The Constant PLANET_WATER. */
	static final int PLANET_WATER = 2;

	/** The Constant PLANET_AIR_TEMP. */
	static final int PLANET_AIR_TEMP = 3;

	/** The Constant PLANET_FLUID_DENSITY. */
	static final int PLANET_FLUID_DENSITY = 4;

	/** The Constant WALL_VIEW_TRANSPARENT. */
	static final int WALL_VIEW_TRANSPARENT = 0;

	/** The Constant WALL_VIEW_SOLID. */
	static final int WALL_VIEW_SOLID = 2;

	/** The acam. */
	static double acam[] = new double[41];

	/** The achrd. */
	static double achrd[] = new double[41];

	/** The acount. */
	static int acount[] = new int[41];

	/** The aftp. */
	static int aftp[] = new int[41];

	/** The Constant AIRFOIL. */
	public static final String AIRFOIL = "Airfoil";

	/** The Constant ALTITUDE. */
	public static final String ALTITUDE = "Altitude";

	/** The alunits. */
	static int alunits[] = new int[41];

	/** The Constant ANGLE. */
	public static final String ANGLE = "Angle";

	/** The aspan. */
	static double aspan[] = new double[41];

	/** The athk. */
	static double athk[] = new double[41];

	/** The bsav. */
	static double bsav[][] = new double[10][810];

	/** The bsavi. */
	static int bsavi[][] = new int[7][810];

	/** The Constant CAMBER. */
	public static final String CAMBER = "Camber";

	/** The Constant CHORD2. */
	public static final String CHORD2 = "Chord";

	/** The Constant CHORD3. */
	public static final String CHORD3 = "% chord";

	/** The Constant DENSITY. */
	public static final String DENSITY = "Density";

	/** The Constant DESIGN_VIEWER_HEIGHT. */
	public static final int DESIGN_VIEWER_HEIGHT = 500;

	/** The Constant DESIGN_VIEWER_WIDTH. */
	public static final int DESIGN_VIEWER_WIDTH = 500;

	/** The Constant DIAMETER. */
	public static final String DIAMETER = "Diameter";

	/** The Constant ELLIPSE. */
	public static final String ELLIPSE = "Ellipse";

	/** The Constant FLOW. */
	public static final String FLOW = "Flow";

	/** The Constant G_CU_M. */
	public static final String G_CU_M = "g/cu m";

	/** The Constant K_PA. */
	public static final String K_PA = " kPa";

	/** The Constant LBS. */
	public static final String LBS = "lbs";

	/** The Constant LBS_FT_2. */
	public static final String LBS_FT_2 = " lbs/ft^2";

	/** The Constant LIFT2. */
	public static final String LIFT2 = "Lift";

	/** The Constant logger. */
	static final Logger logger = LogManager.getLogger(Sys.class.getName());

	/** The Constant MPH. */
	public static final String MPH = "mph";

	/** The Constant OUD_PLOTTER_HEIGHT. */
	public static final int OUD_PLOTTER_HEIGHT = 1000;

	/** The Constant OUD_PLOTTER_WIDTH. */
	public static final int OUD_PLOTTER_WIDTH = 500;

	/** The Constant OUW_PLOTTER_HEIGHT. */
	public static final int OUW_PLOTTER_HEIGHT = 500;

	/** The Constant OUW_PLOTTER_WIDTH. */
	public static final int OUW_PLOTTER_WIDTH = 500;

	/** The Constant PI. */
	private static final double PI = 3.1415926;

	/** The Constant PLATE. */
	public static final String PLATE = "Plate";

	/** The Constant PRESS. */
	public static final String PRESS = "Press";

	/** The Constant PRESSURE. */
	public static final String PRESSURE = "Pressure";

	/** The Constant PROBE_L2_HEIGHT. */
	public static final int PROBE_L2_HEIGHT = 150;

	/** The Constant PROBE_L2_WIDTH. */
	public static final int PROBE_L2_WIDTH = 250;

	/** The Constant PROCESS_PLOTER_WIDTH. */
	public static final int PROCESS_PLOTER_WIDTH = 500;

	/** The Constant PROCESS_PLOTTER_HEIGHT. */
	public static final int PROCESS_PLOTTER_HEIGHT = 500;

	/** The Constant PROCESS_PLOTTER2_HEIGHT. */
	public static final int PROCESS_PLOTTER2_HEIGHT = 500;

	/** The Constant PROCESS_PLOTTER2_WIDTH. */
	public static final int PROCESS_PLOTTER2_WIDTH = 500;

	/** The Constant PROCESS_VIEWER_HEIGHT. */
	public static final int PROCESS_VIEWER_HEIGHT = 500;

	/** The Constant PROCESS_VIEWER_WIDTH. */
	public static final int PROCESS_VIEWER_WIDTH = 500;

	/** The Constant PSI. */
	public static final String PSI = "psi";

	/** The Constant RADIUS_FT. */
	public static final String RADIUS_FT = "Radius ft";

	/** The Constant RESET. */
	public static final String RESET = "Reset";

	/** The Constant RETURN. */
	public static final String RETURN = "Return";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant SPACE_STR. */
	private static final String SPACE_STR = " ";

	/** The Constant THICKNESS_SPACE. */
	public static final String THICKNESS_SPACE = "Thickness ";

	/** The Constant TWELVE_FIVE. */
	public static final String TWELVE_FIVE = "12.5";

	/** The Constant VELOCITY. */
	public static final String VELOCITY = "Velocity";

	/** The Constant WIND_VIEWER_HEIGHT. */
	public static final int WIND_VIEWER_HEIGHT = 500;

	/** The Constant WIND_VIEWER_WIDTH. */
	public static final int WIND_VIEWER_WIDTH = 500;

	/** The Constant WING_AREA. */
	public static final String WING_AREA = "Wing Area";

	/** The Constant X_100. */
	public static final String X_100 = "x 100 ";

	/** The Constant X2. */
	public static final String X2 = " X ";

	/** The Constant ZERO_ZERO. */
	public static final String ZERO_ZERO = "0.0";

	/**
	 * Filter 0.
	 *
	 * @param inumbr
	 *            the inumbr
	 * @return the int
	 */
	public static int filter0(double inumbr) {
		int intermed;

		intermed = (int) inumbr;
		return intermed;
	}

	/**
	 * Filter 3.
	 *
	 * @param inumbr
	 *            the inumbr
	 * @return the float
	 */
	public static float filter3(double inumbr) {
		// output only to .001
		float number;
		int intermed;

		intermed = (int) (inumbr * 1000.);
		number = (float) (intermed / 1000.);
		return number;
	}

	/**
	 * Filter 5.
	 *
	 * @param inumbr
	 *            the inumbr
	 * @return the float
	 */
	public static float filter5(double inumbr) {
		// output only to .00001
		float number;
		int intermed;

		intermed = (int) (inumbr * 100000.);
		number = (float) (intermed / 100000.);
		return number;
	}

	/**
	 * Limit.
	 *
	 * @param low
	 *            the low
	 * @param high
	 *            the high
	 * @param x
	 *            the x
	 * @return the float
	 */
	public static float limit(float low, float high, float x) {
		float result = x;
		if (x > high) {
			result = high;
		} else if (x < low) {
			result = low;
		}
		return result;
	}

	/**
	 * Limit.
	 *
	 * @param low
	 *            the low
	 * @param high
	 *            the high
	 * @param x
	 *            the x
	 * @return the double
	 */
	public static double limit(double low, double high, double x) {
		double result = x;
		if (x > high) {
			result = high;
		} else if (x < low) {
			result = low;
		}
		return result;
	}

	/**
	 * Limit.
	 *
	 * @param low
	 *            the low
	 * @param high
	 *            the high
	 * @param x
	 *            the x
	 * @return the int
	 */
	public static int limit(int low, int high, int x) {
		int result = x;
		if (x > high) {
			result = high;
		} else if (x < low) {
			result = low;
		}
		return result;
	}

	/** The counter. */
	int counter;

	/** The datnum. */
	int datnum;

	/** The off img 1. */
	Image designViewImageBuffer;

	/** The off 1 gg. */
	Graphics designViewImgBuffGraphContext;

	/** The laypan. */
	CardLayout mainPanelLayout;

	/** The pan. */
	MainPanel mainPannel;

	/** The off img 2. */
	Image oudPlotterImageBuffer;

	/** The off 2 gg. */
	Graphics oudPlotterImgBufGraphContext;

	/** The off img 4. */
	Image ouwPlotterImageBuffer;

	/** The off 4 gg. */
	Graphics ouwPlotterImgBufGraphContext;

	/** The off img 5. */
	Image probeL2ImageBuffer;

	/** The off 5 gg. */
	Graphics probeL2ImgBuffGraphContext;

	/** The off 8 gg. */
	Graphics processPlatter2ImgBuffGraphContext;

	/** The off 7 gg. */
	Graphics processPlatterImgBuffGraphContext;

	/** The off img 8. */
	Image processPlotter2ImageBuffer;

	/** The off img 7. */
	Image processPlotterImageBuffer;

	/** The off img 6. */
	Image processViewerImageBuffer;

	/** The off 6 gg. */
	Graphics processViewerImgBufGraphContext;

	/** The off img 3. */
	Image windViewerImageBuffer;

	/** The off 3 gg. */
	Graphics windViewerImgBuffGraphContext;

	/**
	 * Gets the main panel layout.
	 *
	 * @return the main panel layout
	 */
	public CardLayout getMainPanelLayout() {
		return this.mainPanelLayout;
	}

	/**
	 * (non-Javadoc).
	 *
	 * @see java.applet.Applet#init()
	 */
	@Override
	public void init() {
		Sys.logger.info("init");

		enableCrossPlafformLookAndFeel();

		createOffScreenBuffers();

		this.setLayout(new GridLayout(1, 1, 0, 0));

		this.mainPannel = new MainPanel(this);
		this.mainPannel.addComponentListener(this.mainPannel);

		this.add(this.mainPannel);
	}

	/**
	 * Enable cross plafform look and feel.
	 */
	private void enableCrossPlafformLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates the off screen buffers.
	 */
	private void createOffScreenBuffers() {
		// DesignViewer part of Design Card!
		this.designViewImageBuffer = this.createImage(Sys.DESIGN_VIEWER_WIDTH, Sys.DESIGN_VIEWER_HEIGHT);
		this.designViewImgBuffGraphContext = this.designViewImageBuffer.getGraphics();

		// OudPlotter part of Design Card!
		this.oudPlotterImageBuffer = this.createImage(Sys.OUD_PLOTTER_WIDTH, Sys.OUD_PLOTTER_HEIGHT);
		this.oudPlotterImgBufGraphContext = this.oudPlotterImageBuffer.getGraphics();

		// WindViewer part of Wind Card!
		this.windViewerImageBuffer = this.createImage(Sys.WIND_VIEWER_WIDTH, Sys.WIND_VIEWER_HEIGHT);
		this.windViewerImgBuffGraphContext = this.windViewerImageBuffer.getGraphics();

		// OuwPlotter part of Wind Card!
		this.ouwPlotterImageBuffer = this.createImage(Sys.OUW_PLOTTER_WIDTH, Sys.OUW_PLOTTER_HEIGHT);
		this.ouwPlotterImgBufGraphContext = this.ouwPlotterImageBuffer.getGraphics();

		// Probe L2 part of Wind Card!
		this.probeL2ImageBuffer = this.createImage(Sys.PROBE_L2_WIDTH, Sys.PROBE_L2_HEIGHT);
		this.probeL2ImgBuffGraphContext = this.probeL2ImageBuffer.getGraphics();

		// ProcessViewer part of Process Card!
		this.processViewerImageBuffer = this.createImage(Sys.PROCESS_VIEWER_WIDTH, Sys.PROCESS_VIEWER_HEIGHT);
		this.processViewerImgBufGraphContext = this.processViewerImageBuffer.getGraphics();

		// ProcessPlotter part of Process Card!
		this.processPlotterImageBuffer = this.createImage(Sys.PROCESS_PLOTER_WIDTH, Sys.PROCESS_PLOTTER_HEIGHT);
		this.processPlatterImgBuffGraphContext = this.processPlotterImageBuffer.getGraphics();

		// ProcessPlotter2 part of Process Card!
		this.processPlotter2ImageBuffer = this.createImage(Sys.PROCESS_PLOTTER2_WIDTH, Sys.PROCESS_PLOTTER2_HEIGHT);
		this.processPlatter2ImgBuffGraphContext = this.processPlotter2ImageBuffer.getGraphics();
	}

	/**
	 * Sets the main panel layout.
	 *
	 * @param mainPanelLayout
	 *            the new main panel layout
	 */
	public void setMainPanelLayout(CardLayout mainPanelLayout) {

		this.mainPanelLayout = mainPanelLayout;
	}
} // end of Sys