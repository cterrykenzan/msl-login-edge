package com.kenzan.msl.login.edge.services;

import com.google.common.base.Optional;
import com.kenzan.msl.account.client.TestConstants;
import com.kenzan.msl.account.client.services.AccountDataClientService;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import rx.Observable;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AccountDataClientService.class})
public class LoginEdgeServiceImplTest {

  private TestConstants tc = TestConstants.getInstance();
  private AccountDataClientService cassandraAccountService;

  @Before
  public void init() throws Exception {
    cassandraAccountService = EasyMock.createMock(AccountDataClientService.class);
  }

  @Test
  public void testSuccessfulLogIn() {
    mockAuthenticatedMethod();
    EasyMock.replay(cassandraAccountService);
    PowerMock.replayAll();

    LoginEdgeServiceImpl cassandraAuthenticationService =
        new LoginEdgeServiceImpl(cassandraAccountService);
    Observable<Optional<UUID>> results =
        cassandraAuthenticationService.logIn(tc.USER_DTO.getUsername(), tc.USER_DTO.getPassword());
    assertNotNull(results);
    assertEquals(tc.USER_DTO.getUserId(), results.toBlocking().first().get());
  }

  @Test
  public void testUnsuccessfulLogIn() {
    mockAuthenticatedMethod();
    EasyMock.replay(cassandraAccountService);
    PowerMock.replayAll();

    LoginEdgeServiceImpl cassandraAuthenticationService =
        new LoginEdgeServiceImpl(cassandraAccountService);
    Observable<Optional<UUID>> results =
        cassandraAuthenticationService.logIn(tc.USER_DTO.getUsername(), "INVALID_PASSOWRD");
    assertNotNull(results);
    assertFalse(results.toBlocking().first().isPresent());
  }

  @Test
  public void testResetPassword() {
    LoginEdgeServiceImpl cassandraAuthenticationService =
        new LoginEdgeServiceImpl(cassandraAccountService);
    Observable<Void> results =
        cassandraAuthenticationService.resetPassword(tc.USER_DTO.getUsername());
    assertTrue(results.isEmpty().toBlocking().first());

  }

  private void mockAuthenticatedMethod() {
    EasyMock.expect(cassandraAccountService.getUserByUsername(EasyMock.anyString())).andReturn(
        Observable.just(tc.USER_DTO));

  }
}