import { toTitleCase } from './nameHelper';

describe('Name Helper', () => {
  describe('toTitleCase', () => {
    it('returns sentence with the first letter in each word capitalized', () => {
      const sentence = "returns sentence with the first letter in each word capitalized";

      expect(toTitleCase(sentence)).toEqual("Returns Sentence With The First Letter In Each Word Capitalized");
    });
  });
});
