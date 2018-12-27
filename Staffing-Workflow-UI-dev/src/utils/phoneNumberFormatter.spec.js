import { formatPhoneNumber } from './phoneNumberFormatter';

describe('Phone Number Formatter', () => {
  describe('formatPhoneNumber', () => {
    it('returns (123) 456-7890 if passed ', () => {
      expect(formatPhoneNumber("1234567890")).toEqual('(123) 456-7890');
    });

    it('returns null if passed empty string', () => {
      expect(formatPhoneNumber('')).toEqual(null);
    });
  });
});
