import { Issuer } from 'openid-client';

const googleIssuer = await Issuer.discover('https://accounts.google.com');
console.log('Discovered issuer %s %O', googleIssuer.issuer, googleIssuer.metadata);