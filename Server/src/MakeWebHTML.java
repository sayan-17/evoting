//import java.util.ArrayList;
//import java.util.Locale;
//
//public class MakeWebHTML {
//
//
//	private static final int TOTAL = 0;
//	private static final int ACTIVE = 1;
//	private static final int SUCCESSFUL = 2;
//	private static final int UPCOMING = 3;
//
//	public static void setCampaignListHTML(ArrayList<Campaign> campaignList){
//		for(Campaign campaign);
//	}
//
//	public static String getDashboardHTML(String name, ArrayList<String> campaignList, int[] campaignStatusCounter){
//		StringBuilder campaignListString = new StringBuilder("");
//		for(String entry : campaignList){
//			campaignListString.append(entry);
//		}
//		return "<nav class=\"sb-topnav navbar navbar-expand navbar-dark bg-dark\">\n" +
//				"            <!-- Navbar Brand-->\n" +
//				"            <a class=\"navbar-brand ps-3\" href=\"index.html\"><h1><b><i>electio</i></b></h1></a>\n" +
//				"            <!-- Sidebar Toggle-->\n" +
//				"            <button class=\"btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0\" id=\"sidebarToggle\" href=\"#!\"><i class=\"fas fa-bars\"></i></button>\n" +
//				"            <!-- Navbar Search-->\n" +
//				"            <form class=\"d-none d-md-inline-block form-inline ms-auto me-0 me-md-3 my-2 my-md-0\">\n" +
//				"                <div class=\"search-bar\">\n" +
//				"                    <div class=\"input-group rounded\">\n" +
//				"                      <input type=\"search\" class=\"form-control rounded\" placeholder=\"Search Campaigns\" aria-label=\"Search\"\n" +
//				"                        aria-describedby=\"search-addon\" />\n" +
//				"                        &nbsp;\n" +
//				"                      <span class=\"input-group-text border-0\" id=\"search-addon\">\n" +
//				"                        <i class=\"fa fa-search\" aria-hidden=\"true\"></i>\n" +
//				"                      </span>\n" +
//				"                    </div>\n" +
//				"                  </div>\n" +
//				"            </form>\n" +
//				"            \n" +
//				"            <!-- Navbar-->\n" +
//				"            <ul class=\"navbar-nav ms-auto ms-md-0 me-3 me-lg-4\">\n" +
//				"                <li class=\"nav-item dropdown\">\n" +
//				"                    <span class=\"initials\">" + name.toUpperCase(Locale.ROOT).charAt(0) + "</span>\n" +
//				"                </li>\n" +
//				"            </ul>\n" +
//				"        </nav>\n" +
//				"        <div id=\"layoutSidenav\">\n" +
//				"            <div id=\"layoutSidenav_nav\">\n" +
//				"                <nav class=\"sb-sidenav accordion sb-sidenav-dark\" id=\"sidenavAccordion\">\n" +
//				"                    <div class=\"sb-sidenav-menu\">\n" +
//				"                        <div class=\"nav\">\n" +
//				"                            <div class=\"sb-sidenav-menu-heading\">Core</div>\n" +
//				"                            <a class=\"nav-link active-selection\" href=\"admin-dashboard.html\">\n" +
//				"                                <div class=\"sb-nav-link-icon\"><i class=\"fas fa-tachometer-alt\"></i></div>\n" +
//				"                                Dashboard\n" +
//				"                            </a>\n" +
//				"                            <div class=\"sb-sidenav-menu-heading\">Analytics</div>\n" +
//				"                            <a class=\"nav-link collapsed\" href=\"#\" data-bs-toggle=\"collapse\" data-bs-target=\"#collapseLayouts\" aria-expanded=\"false\" aria-controls=\"collapseLayouts\">\n" +
//				"                                <div class=\"sb-nav-link-icon\"><i class=\"fas fa-columns\"></i></div>\n" +
//				"                                Campaigns\n" +
//				"                                <div class=\"sb-sidenav-collapse-arrow\"><i class=\"fas fa-angle-down\"></i></div>\n" +
//				"                            </a>\n" +
//				"                            <div class=\"collapse\" id=\"collapseLayouts\" aria-labelledby=\"headingOne\" data-bs-parent=\"#sidenavAccordion\">\n" +
//				"                                <nav class=\"sb-sidenav-menu-nested nav\">\n" +
//				"                                    <a class=\"nav-link\" href=\"admin-all-campaigns.html\">All Campaigns</a>\n" +
//				"                                    <a class=\"nav-link\" href=\"admin-active-campaigns.html\">Active Campaigns</a>\n" +
//				"                                    <a class=\"nav-link\" href=\"admin-successful-campaigns.html\">Successful Campaigns</a>\n" +
//				"                                    <a class=\"nav-link\" href=\"admin-upcoming-campaigns.html\">Upcoming Campaigns</a>\n" +
//				"                                </nav>\n" +
//				"                            </div>\n" +
//				"                            \n" +
//				"                            <div class=\"sb-sidenav-menu-heading\">Account</div>\n" +
//				"                            <a class=\"nav-link collapsed\" href=\"#\" data-bs-toggle=\"collapse\" data-bs-target=\"#collapsePages\" aria-expanded=\"false\" aria-controls=\"collapsePages\">\n" +
//				"                                <div class=\"sb-nav-link-icon\"><i class=\"fas fa-book-open\"></i></div>\n" +
//				"                                Settings\n" +
//				"                                <div class=\"sb-sidenav-collapse-arrow\"><i class=\"fas fa-angle-down\"></i></div>\n" +
//				"                            </a>\n" +
//				"                            <div class=\"collapse\" id=\"collapsePages\" aria-labelledby=\"headingTwo\" data-bs-parent=\"#sidenavAccordion\">\n" +
//				"                                <nav class=\"sb-sidenav-menu-nested nav accordion\" id=\"sidenavAccordionPages\">\n" +
//				"                                    <a class=\"nav-link collapsed\" href=\"#\" data-bs-toggle=\"collapse\" data-bs-target=\"#pagesCollapseAuth\" aria-expanded=\"false\" aria-controls=\"pagesCollapseAuth\">\n" +
//				"                                        Change Name\n" +
//				"                                    </a>\n" +
//				"                                    <a class=\"nav-link collapsed\" href=\"#\" data-bs-toggle=\"collapse\" data-bs-target=\"#pagesCollapseAuth\" aria-expanded=\"false\" aria-controls=\"pagesCollapseAuth\">\n" +
//				"                                        Change email\n" +
//				"                                    </a>\n" +
//				"                                    <a class=\"nav-link collapsed\" href=\"#\" data-bs-toggle=\"collapse\" data-bs-target=\"#pagesCollapseAuth\" aria-expanded=\"false\" aria-controls=\"pagesCollapseAuth\">\n" +
//				"                                        Reset Password\n" +
//				"                                    </a>\n" +
//				"                                    <a class=\"nav-link collapsed\" href=\"#\" data-bs-toggle=\"collapse\" data-bs-target=\"#pagesCollapseAuth\" aria-expanded=\"false\" aria-controls=\"pagesCollapseAuth\">\n" +
//				"                                        Delete Account\n" +
//				"                                    </a>\n" +
//				"                                </nav>\n" +
//				"                            </div>\n" +
//				"                                <a class=\"nav-link\" onclick=\"confirmLogout()\" href=\"index.html\" >\n" +
//				"                                    <div class=\"sb-nav-link-icon\"><i class=\"fa fa-power-off\" aria-hidden=\"true\"></i></i></i></div>\n" +
//				"                                    <div class=\"logout-link\">Logout</div>\n" +
//				"                                </a>\n" +
//				"                            </div>\n" +
//				"                    </div>\n" +
//				"                    <div class=\"sb-sidenav-footer\">\n" +
//				"                        <div class=\"small\">Need Help ?</div>\n" +
//				"                        <a href=\"#\">See User Guide</a>\n" +
//				"                    </div>\n" +
//				"                </nav>\n" +
//				"            </div>\n" +
//				"            <div id=\"layoutSidenav_content\">\n" +
//				"                <main>\n" +
//				"                    <div class=\"container-fluid px-4\">\n" +
//				"                        <h1 class=\"mt-4\"><b>" + name + "'s Dashboard</b></h1>\n" +
//				"                        <ol class=\"breadcrumb mb-4\">\n" +
//				"                            <li class=\"breadcrumb-item active\"><b>Overall</b></li>\n" +
//				"                        </ol>\n" +
//				"                        <div class=\"row\">\n" +
//				"                            <div class=\"col-xl-3 col-md-6\">\n" +
//				"                                <div class=\"card bg-primary text-white mb-4\">\n" +
//				"                                    <div class=\"card-header\">Total Campaigns</div>\n" +
//				"                                    <div class=\"class-body\" style=\"align-self: center;\" id=\"admin-dashboard-total-campaigns\"><h1>" + campaignStatusCounter[TOTAL] + "</h1></div>\n" +
//				"                                    \n" +
//				"                                </div>\n" +
//				"                            </div>\n" +
//				"                            <div class=\"col-xl-3 col-md-6\">\n" +
//				"                                <div class=\"card bg-primary text-white mb-4\">\n" +
//				"                                    <div class=\"card-header\">Active Campaigns</div>\n" +
//				"                                    <div class=\"class-body\" style=\"align-self: center;\" id=\"admin-dashboard-active-campaigns\"><h1>" + campaignStatusCounter[ACTIVE] + "</h1></div>\n" +
//				"                                    \n" +
//				"                                </div>\n" +
//				"                            </div>\n" +
//				"                            <div class=\"col-xl-3 col-md-6\">\n" +
//				"                                <div class=\"card bg-primary text-white mb-4\">\n" +
//				"                                    <div class=\"card-header\">Successful Campaigns</div>\n" +
//				"                                    <div class=\"class-body\" style=\"align-self: center;\" id=\"admin-dashboard-successful-campaigns\"><h1>" + campaignStatusCounter[SUCCESSFUL] + "</h1></div>\n" +
//				"                                    \n" +
//				"                                </div>\n" +
//				"                            </div>\n" +
//				"                            <div class=\"col-xl-3 col-md-6\">\n" +
//				"                                <div class=\"card bg-primary text-white mb-4\">\n" +
//				"                                    <div class=\"card-header\">Upcoming Campaigns</div>\n" +
//				"                                    <div class=\"class-body\" style=\"align-self: center;\" id=\"admin-dashboard-upcoming-campaigns\"><h1>" + campaignStatusCounter[UPCOMING] + "</h1></div>\n" +
//				"                                    \n" +
//				"                                </div>\n" +
//				"                            </div>\n" +
//				"                        </div>\n" +
//				"                        <ol class=\"breadcrumb mb-4\">\n" +
//				"                            <li class=\"breadcrumb-item active\"><b>Campaign details</b></li>\n" +
//				"                        </ol>\n" +
//				"                        <div class=\"card mb-4\">\n" +
//				"                            <div class=\"card-header\">\n" +
//				"                                <p id=\"campaign-table-heading\"><i class=\"fas fa-table me-1\"></i> <b>All Campaigns</b></p>\n" +
//				"                            </div>\n" +
//				"                            <div class=\"card-body campaign-list\" id=\"dashboard-campaign-list\">\n" +
//				                                campaignListString +
//				"                            </div>\n" +
//				"\n" +
//				"                        </div>\n" +
//				"                    </div>\n" +
//				"                </main>\n" +
//				"                <footer class=\"py-4 bg-dark-footer mt-auto\">\n" +
//				"                    <div class=\"container-fluid px-4\">\n" +
//				"                        <div class=\"d-flex align-items-center justify-content-between small\">\n" +
//				"                            <div class=\"text-muted\">This is a personal project.</div>\n" +
//				"                            <div class=\"text-muted\">\n" +
//				"                                <a href=\"https://github.com/sayan-17/evoting\" target=\"_blank\">Source code</a>\n" +
//				"                                &middot;\n" +
//				"                                <a href=\"https://www.linkedin.com/in/sayanpaul17/\" target=\"_blank\">Developer's Linkedin</a>\n" +
//				"                            </div>\n" +
//				"                        </div>\n" +
//				"                    </div>\n" +
//				"                </footer>\n" +
//				"            </div>\n" +
//				"        </div>";
//	}
//}
