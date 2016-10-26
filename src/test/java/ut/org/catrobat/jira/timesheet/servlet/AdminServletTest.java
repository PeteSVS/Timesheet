package ut.org.catrobat.jira.timesheet.servlet;

import com.atlassian.activeobjects.test.TestActiveObjects;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.websudo.WebSudoManager;
import com.atlassian.templaterenderer.TemplateRenderer;
import net.java.ao.EntityManager;
import net.java.ao.test.jdbc.Data;
import net.java.ao.test.junit.ActiveObjectsJUnitRunner;
import org.catrobat.jira.timesheet.activeobjects.ConfigService;
import org.catrobat.jira.timesheet.activeobjects.impl.ConfigServiceImpl;
import org.catrobat.jira.timesheet.services.CategoryService;
import org.catrobat.jira.timesheet.services.PermissionService;
import org.catrobat.jira.timesheet.services.TeamService;
import org.catrobat.jira.timesheet.services.impl.CategoryServiceImpl;
import org.catrobat.jira.timesheet.services.impl.PermissionServiceImpl;
import org.catrobat.jira.timesheet.services.impl.TeamServiceImpl;
import org.catrobat.jira.timesheet.servlet.AdminServlet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import ut.org.catrobat.jira.timesheet.activeobjects.MySampleDatabaseUpdater;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(ActiveObjectsJUnitRunner.class)
@Data(MySampleDatabaseUpdater.class)
@PrepareForTest(ComponentAccessor.class)
public class AdminServletTest {
    String test_key = "test_key";
    private AdminServlet adminServlet;
    private LoginUriProvider loginUriProviderMock;
    private TemplateRenderer templateRendererMock;
    private PermissionService permissionServiceMock;
    private WebSudoManager webSudoManagerMock;
    private ConfigService configServiceMock;
    private ComponentAccessor componentAccessorMock;
    private TeamService teamService;
    private PermissionService permissionService;
    private ConfigService configService;
    private CategoryService categoryService;
    private TestActiveObjects ao;
    private EntityManager entityManager;
    private HttpServletResponse response;
    private HttpServletRequest request;
    private ApplicationUser userMock;
    private JiraAuthenticationContext jiraAuthenticationContext;

    @Before
    public void setUp() throws Exception {
        assertNotNull(entityManager);
        ao = new TestActiveObjects(entityManager);

        PowerMockito.mockStatic(ComponentAccessor.class);

        loginUriProviderMock = mock(LoginUriProvider.class, RETURNS_DEEP_STUBS);
        templateRendererMock = mock(TemplateRenderer.class, RETURNS_DEEP_STUBS);
        webSudoManagerMock = mock(WebSudoManager.class, RETURNS_DEEP_STUBS);
        permissionServiceMock = mock(PermissionService.class, RETURNS_DEEP_STUBS);
        userMock = mock(ApplicationUser.class, RETURNS_DEEP_STUBS);
        request = mock(HttpServletRequest.class, RETURNS_DEEP_STUBS);
        response = mock(HttpServletResponse.class, RETURNS_DEEP_STUBS);
        jiraAuthenticationContext = mock(JiraAuthenticationContext.class, RETURNS_DEEP_STUBS);

        categoryService = new CategoryServiceImpl(ao);
        configService = new ConfigServiceImpl(ao, categoryService);
        teamService = new TeamServiceImpl(ao, configService);
        permissionService = new PermissionServiceImpl(teamService, configService);

        adminServlet = new AdminServlet(loginUriProviderMock, templateRendererMock, webSudoManagerMock, permissionServiceMock);

        when(userMock.getUsername()).thenReturn("test");
        when(userMock.getKey()).thenReturn(test_key);

        when(permissionServiceMock.checkIfUserIsGroupMember(request, "jira-administrators")).thenReturn(false);
        when(permissionServiceMock.checkIfUserIsGroupMember(request, "Timesheet")).thenReturn(true);

        PowerMockito.when(ComponentAccessor.getJiraAuthenticationContext()).thenReturn(jiraAuthenticationContext);
        PowerMockito.when(jiraAuthenticationContext.getLoggedInUser()).thenReturn(userMock);
    }

    @Test
    public void testDoGet() throws Exception {
        adminServlet.doGet(request, response);
    }

}